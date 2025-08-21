package com.example.service.impl;

import com.example.entity.Account;
import com.example.entity.AuditLog;
import com.example.entity.Transaction;
import com.example.entity.auditLog.AccountAuditLog;
import com.example.entity.auditLog.TransactionAuditLog;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.enums.AuditAction;
import com.example.entity.enums.TransactionStatus;
import com.example.entity.projection.TransactionSummary;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import com.example.entity.response.TotalAmountTransactionResponse;
import com.example.exception.TransactionAmountExceed;
import com.example.exception.TransactionDeclinedException;
import com.example.exception.TransactionException;
import com.example.mapper.AccountMapper;
import com.example.mapper.TransactionMapper;
import com.example.repository.AccountRepository;
import com.example.repository.AuditLogRepository;
import com.example.repository.TransactionRepository;
import com.example.repository.spec.TransactionSpecification;
import com.example.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final AuditLogRepository auditLogRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountMapper accountMapper, AccountRepository accountRepository, AuditLogRepository auditLogRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, noRollbackFor = {TransactionException.class}, transactionManager = "jpaTransactionManager")
    public CreateTransactionResponse createSingleTransaction(CreateTransactionRequest createTransactionRequest) {
        return createTransaction(createTransactionRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, noRollbackFor = {TransactionException.class}, transactionManager = "jpaTransactionManager")
    public CreateTransactionResponse createSingleTransactionWithKafka(CreateTransactionRequest createTransactionRequest) {
        Transaction transactionProcessing = transactionMapper.createTransactionRequestToTransaction(createTransactionRequest);
        transactionProcessing.setTransactionStatus(TransactionStatus.PENDING);
        transactionProcessing.setActiveStatus(ActiveStatus.ACTIVE);

        TransactionAuditLog transactionAuditLog = transactionMapper.transactionToTransactionAuditLog(transactionProcessing);
//        createLogAudit(transactionAuditLog, AuditAction.TRANSFER_FUNDS);

        transactionProcessing = transactionRepository.save(transactionProcessing);

        Account fromAccount = transactionProcessing.getFromAccount();
        Account toAccount = transactionProcessing.getToAccount();

        try {

            //Transfer money from account to account
            AccountAuditLog fromAccountAuditLog = accountMapper.accountToAccountAuditLog(fromAccount);
            fromAccountAuditLog.setCurrentAmount(fromAccount.getAmount());
            fromAccountAuditLog.setAmountTransferred(transactionProcessing.getAmount());
//            createLogAudit(fromAccountAuditLog, AuditAction.ACCOUNT_AMOUNT_CHANGE);

            sendAccountMoney(fromAccount, transactionProcessing.getAmount());

            AccountAuditLog toAccountAuditLog = accountMapper.accountToAccountAuditLog(toAccount);
            toAccountAuditLog.setCurrentAmount(toAccount.getAmount());
            toAccountAuditLog.setAmountReceived(transactionProcessing.getAmount());
//            createLogAudit(toAccountAuditLog, AuditAction.ACCOUNT_AMOUNT_CHANGE);

            receiveAccountMoney(toAccount, transactionProcessing.getAmount());


        } catch (TransactionDeclinedException transactionDeclinedException) {
            transactionProcessing.setTransactionStatus(TransactionStatus.DECLINED);
            transactionProcessing = transactionRepository.save(transactionProcessing);

            transactionAuditLog = transactionMapper.transactionToTransactionAuditLog(transactionProcessing);
//            createLogAudit(transactionAuditLog, AuditAction.TRANSFER_FUNDS);

            CreateTransactionResponse createTransactionResponse = transactionMapper.transactionToCreateTransactionResponse(transactionProcessing);
            createTransactionResponse.setFailMessage(transactionDeclinedException.getMessage());
            return createTransactionResponse;
        }

        transactionProcessing.setTransactionStatus(TransactionStatus.APPROVED);
        transactionAuditLog = transactionMapper.transactionToTransactionAuditLog(transactionProcessing);
//        createLogAudit(transactionAuditLog, AuditAction.TRANSFER_FUNDS);

        transactionProcessing = transactionRepository.save(transactionProcessing);

        return transactionMapper.transactionToCreateTransactionResponse(transactionProcessing);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, noRollbackFor = {TransactionException.class}, transactionManager = "jpaTransactionManager")
    public List<CreateTransactionResponse> createMultipleTransaction(List<CreateTransactionRequest> createTransactionRequests) {

        List<CreateTransactionResponse> createTransactionResponses = new ArrayList<>();
        for (CreateTransactionRequest createTransactionRequest : createTransactionRequests) {
            try {
                CreateTransactionResponse response = this.createTransaction(createTransactionRequest);
                createTransactionResponses.add(response);
            } catch (Exception ignored) {
            }
        }

        return createTransactionResponses;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class, noRollbackFor = {TransactionException.class}, transactionManager = "jpaTransactionManager")
    public CreateTransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {

        Transaction transactionProcessing = transactionMapper.createTransactionRequestToTransaction(createTransactionRequest);
        transactionProcessing.setTransactionStatus(TransactionStatus.PENDING);
        transactionProcessing.setActiveStatus(ActiveStatus.ACTIVE);

        TransactionAuditLog transactionAuditLog = transactionMapper.transactionToTransactionAuditLog(transactionProcessing);
        createLogAudit(transactionAuditLog, AuditAction.TRANSFER_FUNDS);

        transactionProcessing = transactionRepository.save(transactionProcessing);

        Account fromAccount = transactionProcessing.getFromAccount();
        Account toAccount = transactionProcessing.getToAccount();

        try {

            //Transfer money from account to account
            AccountAuditLog fromAccountAuditLog = accountMapper.accountToAccountAuditLog(fromAccount);
            fromAccountAuditLog.setCurrentAmount(fromAccount.getAmount());
            fromAccountAuditLog.setAmountTransferred(transactionProcessing.getAmount());
            createLogAudit(fromAccountAuditLog, AuditAction.ACCOUNT_AMOUNT_CHANGE);

            sendAccountMoney(fromAccount, transactionProcessing.getAmount());

            AccountAuditLog toAccountAuditLog = accountMapper.accountToAccountAuditLog(toAccount);
            toAccountAuditLog.setCurrentAmount(toAccount.getAmount());
            toAccountAuditLog.setAmountReceived(transactionProcessing.getAmount());
            createLogAudit(toAccountAuditLog, AuditAction.ACCOUNT_AMOUNT_CHANGE);

            receiveAccountMoney(toAccount, transactionProcessing.getAmount());


        } catch (TransactionDeclinedException transactionDeclinedException) {
            transactionProcessing.setTransactionStatus(TransactionStatus.DECLINED);
            transactionProcessing = transactionRepository.save(transactionProcessing);

            transactionAuditLog = transactionMapper.transactionToTransactionAuditLog(transactionProcessing);
            createLogAudit(transactionAuditLog, AuditAction.TRANSFER_FUNDS);

            CreateTransactionResponse createTransactionResponse = transactionMapper.transactionToCreateTransactionResponse(transactionProcessing);
            createTransactionResponse.setFailMessage(transactionDeclinedException.getMessage());
            return createTransactionResponse;
        }

        transactionProcessing.setTransactionStatus(TransactionStatus.APPROVED);
        transactionAuditLog = transactionMapper.transactionToTransactionAuditLog(transactionProcessing);
        createLogAudit(transactionAuditLog, AuditAction.TRANSFER_FUNDS);

        transactionProcessing = transactionRepository.save(transactionProcessing);

        return transactionMapper.transactionToCreateTransactionResponse(transactionProcessing);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, noRollbackFor = {}, transactionManager = "jpaTransactionManager")
    public void createLogAudit(Object objectChanged, AuditAction auditAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String jsonValue = objectMapper.writeValueAsString(objectChanged);
            AuditLog auditLog = AuditLog.builder().data(jsonValue).action(auditAction).build();
            auditLogRepository.save(auditLog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class, noRollbackFor = {}, transactionManager = "jpaTransactionManager")
    protected void sendAccountMoney(Account accountSend, BigDecimal amount) {
        if (accountSend.getAmount().compareTo(amount) < 0) {
            throw new TransactionAmountExceed("Amount transfer exceed current account amount with accountId " + accountSend.getId() + ", amount transfer: " + amount);
        }
        accountSend.setAmount(accountSend.getAmount().subtract(amount));
        accountRepository.save(accountSend);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class, noRollbackFor = {}, transactionManager = "jpaTransactionManager")
    protected void receiveAccountMoney(Account accountReceive, BigDecimal amount) {
        accountReceive.setAmount(accountReceive.getAmount().add(amount));
        accountRepository.save(accountReceive);
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public List<FilterTransactionResponse> getTransactionHistoryByCreatedTime(LocalDateTime createTime) {
        if (createTime == null) return List.of();
        List<Transaction> transactionList = transactionRepository.findByCreatedTimeAfter(createTime);
        return transactionList.stream().map(transactionMapper::transactionToFilterTransactionResponse).toList();
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public TotalAmountTransactionResponse getTotalAmountTransactionByTime(LocalDateTime time) {
        BigDecimal totalAmount = transactionRepository.findAmountTransferByTime(time, time.plusDays(7));
        return new TotalAmountTransactionResponse(time, totalAmount.toPlainString());
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public Page<FilterTransactionResponse> getTransactionHistoryByAccountId(String accountId, Pageable pageable) {
        Account account = accountRepository.findAccountById(UUID.fromString(accountId)).orElse(null);
        Page<Transaction> transactionResponses = transactionRepository.findByFromAccountOrToAccount(account, account, pageable);
        return transactionResponses.map(transactionMapper::transactionToFilterTransactionResponse);
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public Window<FilterTransactionResponse> getTransactionHistoryByAccountId(String accountId, ScrollPosition scrollPosition) {
        Account account = accountRepository.findAccountById(UUID.fromString(accountId)).orElse(null);
        Window<Transaction> transactionResponses = transactionRepository.findFirst3ByFromAccountOrToAccountOrderByCreatedTime(account, account, scrollPosition);
        return transactionResponses.map(transactionMapper::transactionToFilterTransactionResponse);
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public List<FilterTransactionResponse> search(FilterTransactionRequest filterTransactionRequest) {
        Specification<Transaction> filterTransactionSpecification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (filterTransactionRequest != null) {
            if (filterTransactionRequest.getStatusList() != null) {
                for (TransactionStatus status : filterTransactionRequest.getStatusList()) {
                    filterTransactionSpecification = filterTransactionSpecification.or(TransactionSpecification.hasStatus(status));
                }
            }
            if (filterTransactionRequest.getAmountMin() != null) {
                filterTransactionSpecification = filterTransactionSpecification.and(TransactionSpecification.hasAmountGreaterThanOrEqual(filterTransactionRequest.getAmountMin()));
            }
            if (filterTransactionRequest.getAmountMax() != null) {
                filterTransactionSpecification = filterTransactionSpecification.and(TransactionSpecification.hasAmountLessThanOrEqual(filterTransactionRequest.getAmountMax()));
            }
            if (filterTransactionRequest.getCreatedDateFrom() != null) {
                filterTransactionSpecification = filterTransactionSpecification.and(TransactionSpecification.createdAfter(filterTransactionRequest.getCreatedDateFrom()));
            }
            if (filterTransactionRequest.getCreatedDateTo() != null) {
                filterTransactionSpecification = filterTransactionSpecification.and(TransactionSpecification.createdBefore(filterTransactionRequest.getCreatedDateTo()));
            }
        }

        List<Transaction> transactionList = transactionRepository.findAll(filterTransactionSpecification);

        return transactionList.stream().map(transactionMapper::transactionToFilterTransactionResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public List<TransactionSummary> getListTransactionTransfer(String fromAccountId) {
        Account account = accountRepository.findAccountById(UUID.fromString(fromAccountId)).orElse(null);
        return transactionRepository.findByFromAccount(account);
    }

}
