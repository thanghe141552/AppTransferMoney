package com.example.service;

import com.example.entity.Account;
import com.example.entity.Transaction;
import com.example.entity.auditLog.AccountAuditLog;
import com.example.entity.auditLog.TransactionAuditLog;
import com.example.entity.enums.TransactionStatus;
import com.example.entity.projection.TransactionSummary;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import com.example.entity.response.TotalAmountTransactionResponse;
import com.example.mapper.AccountMapper;
import com.example.mapper.TransactionMapper;
import com.example.repository.AccountRepository;
import com.example.repository.AuditLogRepository;
import com.example.repository.TransactionRepository;
import com.example.service.impl.TransactionServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = TransactionServiceImpl.class)
class TransactionServiceImplTest {

    @Autowired
    private TransactionServiceImpl transactionService;

    @MockitoBean
    private TransactionRepository transactionRepository;

    @MockitoBean
    private TransactionMapper transactionMapper;

    @MockitoBean
    private AccountMapper accountMapper;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private AuditLogRepository auditLogRepository;

    @Test
    void createSingleTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        CreateTransactionResponse expectedResponse = new CreateTransactionResponse();
        Transaction transaction = new Transaction();

        Account accountFrom = new Account();
        accountFrom.setId(UUID.randomUUID());
        accountFrom.setAmount(BigDecimal.valueOf(500));

        Account accountTo = new Account();
        accountTo.setId(UUID.randomUUID());
        accountTo.setAmount(BigDecimal.valueOf(200));

        transaction.setFromAccount(accountFrom);
        transaction.setToAccount(accountTo);
        transaction.setAmount(BigDecimal.valueOf(100));

        TransactionAuditLog transactionAuditLog = new TransactionAuditLog();
        transactionAuditLog.setFromAccountId(accountFrom.getId().toString());
        transactionAuditLog.setToAccountId(accountTo.getId().toString());
        transactionAuditLog.setAmount(transaction.getAmount());

        AccountAuditLog accountFromAuditLog = new AccountAuditLog();
        accountFromAuditLog.setCurrentAmount(accountFrom.getAmount());
        accountFromAuditLog.setAccountId(accountFrom.getId().toString());
        accountFromAuditLog.setAmountTransferred(transaction.getAmount());

        AccountAuditLog accountToAuditLog = new AccountAuditLog();
        accountToAuditLog.setCurrentAmount(accountTo.getAmount());
        accountToAuditLog.setAccountId(accountTo.getId().toString());
        accountToAuditLog.setAmountReceived(transaction.getAmount());


        Mockito.when(transactionMapper.createTransactionRequestToTransaction(request)).thenReturn(transaction);
        Mockito.when(transactionMapper.transactionToTransactionAuditLog(transaction)).thenReturn(transactionAuditLog);
        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
        Mockito.when(accountMapper.accountToAccountAuditLog(accountFrom)).thenReturn(accountFromAuditLog);
        Mockito.when(accountMapper.accountToAccountAuditLog(accountTo)).thenReturn(accountToAuditLog);
        Mockito.when(transactionMapper.transactionToCreateTransactionResponse(transaction)).thenReturn(expectedResponse);

        CreateTransactionResponse actual = transactionService.createSingleTransaction(request);

        Assertions.assertThat(expectedResponse).isEqualTo(actual);
        Mockito.verify(transactionRepository, times(2)).save(Mockito.any());
        Mockito.verify(auditLogRepository, atLeastOnce()).save(Mockito.any());
    }

    @Test
    void createMultipleTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest();

        List<CreateTransactionRequest> requests = List.of(request, request);

        CreateTransactionResponse expectedResponse = new CreateTransactionResponse();
        Transaction transaction = new Transaction();

        Account accountFrom = new Account();
        accountFrom.setId(UUID.randomUUID());
        accountFrom.setAmount(BigDecimal.valueOf(500));

        Account accountTo = new Account();
        accountTo.setId(UUID.randomUUID());
        accountTo.setAmount(BigDecimal.valueOf(200));

        transaction.setFromAccount(accountFrom);
        transaction.setToAccount(accountTo);
        transaction.setAmount(BigDecimal.valueOf(100));

        TransactionAuditLog transactionAuditLog = new TransactionAuditLog();
        transactionAuditLog.setFromAccountId(accountFrom.getId().toString());
        transactionAuditLog.setToAccountId(accountTo.getId().toString());
        transactionAuditLog.setAmount(transaction.getAmount());

        AccountAuditLog accountFromAuditLog = new AccountAuditLog();
        accountFromAuditLog.setCurrentAmount(accountFrom.getAmount());
        accountFromAuditLog.setAccountId(accountFrom.getId().toString());
        accountFromAuditLog.setAmountTransferred(transaction.getAmount());

        AccountAuditLog accountToAuditLog = new AccountAuditLog();
        accountToAuditLog.setCurrentAmount(accountTo.getAmount());
        accountToAuditLog.setAccountId(accountTo.getId().toString());
        accountToAuditLog.setAmountReceived(transaction.getAmount());


        Mockito.when(transactionMapper.createTransactionRequestToTransaction(request)).thenReturn(transaction);
        Mockito.when(transactionMapper.transactionToTransactionAuditLog(transaction)).thenReturn(transactionAuditLog);
        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
        Mockito.when(accountMapper.accountToAccountAuditLog(accountFrom)).thenReturn(accountFromAuditLog);
        Mockito.when(accountMapper.accountToAccountAuditLog(accountTo)).thenReturn(accountToAuditLog);
        Mockito.when(transactionMapper.transactionToCreateTransactionResponse(transaction)).thenReturn(expectedResponse);

        List<CreateTransactionResponse> result = transactionService.createMultipleTransaction(requests);

        Mockito.verify(transactionRepository, times(4)).save(Mockito.any());
        Mockito.verify(auditLogRepository, atLeastOnce()).save(Mockito.any());
        Assertions.assertThat(2).isEqualTo(result.size());
        Assertions.assertThat(expectedResponse).isEqualTo(result.getFirst());
    }

    @Test
    void getTransactionHistory() {
        List<Transaction> transactions = List.of(new Transaction());
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionHistory();

        Assertions.assertThat(transactions).isEqualTo(result);
    }

    @Test
    void getTransactionHistoryByCreatedTime() {
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        Transaction transaction = new Transaction();
        FilterTransactionResponse response = new FilterTransactionResponse();
        Mockito.when(transactionRepository.findByCreatedTimeAfter(time)).thenReturn(List.of(transaction));
        Mockito.when(transactionMapper.transactionToFilterTransactionResponse(transaction)).thenReturn(response);

        List<FilterTransactionResponse> result = transactionService.getTransactionHistoryByCreatedTime(time);


        Assertions.assertThat(1).isEqualTo(result.size());
        Assertions.assertThat(response).isEqualTo(result.getFirst());
    }

    @Test
    void getTransactionHistoryByCreatedTime_withNull() {
        List<FilterTransactionResponse> result = transactionService.getTransactionHistoryByCreatedTime(null);
        Assertions.assertThat(true).isEqualTo(result.isEmpty());
    }

    @Test
    void getTotalAmountTransactionByTime() {
        LocalDateTime time = LocalDateTime.now();
        Mockito.when(transactionRepository.findAmountTransferByTime(time, time.plusDays(7))).thenReturn(BigDecimal.TEN);

        TotalAmountTransactionResponse result = transactionService.getTotalAmountTransactionByTime(time);

        Assertions.assertThat("10").isEqualTo(result.getTotalAmount());
    }

    @Test
    void getListTransactionTransfer() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);

        List<TransactionSummary> summaries = List.of(Mockito.mock(TransactionSummary.class));

        Mockito.when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findByFromAccount(account)).thenReturn(summaries);

        List<TransactionSummary> result = transactionService.getListTransactionTransfer(accountId.toString());

        Assertions.assertThat(summaries).isEqualTo(result);
    }

    @Test
    void getTransactionHistoryByAccountId_scroll() {
        // Given
        String accountId = UUID.randomUUID().toString();
        Account account = new Account(); // set required fields if needed
        ScrollPosition scrollPosition = ScrollPosition.offset(0);

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        FilterTransactionResponse response1 = new FilterTransactionResponse();
        FilterTransactionResponse response2 = new FilterTransactionResponse();

        Window<Transaction> transactionWindow = Window.from(List.of(transaction1, transaction2), pos -> scrollPosition);
        Window<FilterTransactionResponse> expectedWindow = Window.from(List.of(response1, response2), pos -> scrollPosition);

        Mockito.when(accountRepository.findAccountById(UUID.fromString(accountId))).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findFirst3ByFromAccountOrToAccountOrderByCreatedTime(account, account, scrollPosition)).thenReturn(transactionWindow);
        Mockito.when(transactionMapper.transactionToFilterTransactionResponse(transaction1)).thenReturn(response1);
        Mockito.when(transactionMapper.transactionToFilterTransactionResponse(transaction2)).thenReturn(response2);

        // When
        Window<FilterTransactionResponse> result = transactionService.getTransactionHistoryByAccountId(accountId, scrollPosition);

        // Then
        Assertions.assertThat(result.getContent()).isEqualTo(expectedWindow.getContent());
        Mockito.verify(accountRepository).findAccountById(UUID.fromString(accountId));
        Mockito.verify(transactionRepository).findFirst3ByFromAccountOrToAccountOrderByCreatedTime(account, account, scrollPosition);
    }

    @Test
    void getTransactionHistoryByAccountId_pageable() {

        // Given
        String accountId = UUID.randomUUID().toString();
        Account account = new Account();

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();

        FilterTransactionResponse response1 = new FilterTransactionResponse();
        FilterTransactionResponse response2 = new FilterTransactionResponse();

        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction1, transaction2));
        Page<FilterTransactionResponse> expectedPage = new PageImpl<>(List.of(response1, response2));;

        Mockito.when(accountRepository.findAccountById(UUID.fromString(accountId))).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findByFromAccountOrToAccount(account, account, PageRequest.of(0, 10))).thenReturn(transactionPage);
        Mockito.when(transactionMapper.transactionToFilterTransactionResponse(transaction1)).thenReturn(response1);
        Mockito.when(transactionMapper.transactionToFilterTransactionResponse(transaction2)).thenReturn(response2);

        // When
        Page<FilterTransactionResponse> result = transactionService.getTransactionHistoryByAccountId(accountId, PageRequest.of(0, 10));

        // Then
        Assertions.assertThat(result.getContent()).isEqualTo(expectedPage.getContent());
        Mockito.verify(accountRepository).findAccountById(UUID.fromString(accountId));
        Mockito.verify(transactionRepository).findByFromAccountOrToAccount(account, account, PageRequest.of(0, 10));
    }

    @Test
    void getTransactionHistoryByAccountId_pageable_nullResult() {
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);

        Mockito.when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findByFromAccountOrToAccount(eq(account), eq(account), (Pageable) any())).thenReturn(Page.empty());

        Page<FilterTransactionResponse> result = transactionService.getTransactionHistoryByAccountId(accountId.toString(), PageRequest.of(0, 10));

        Assertions.assertThat(true).isEqualTo(result.isEmpty());
    }

    @Test
    void search() {
        // Given
        FilterTransactionRequest request = new FilterTransactionRequest();
        request.setAmountMin(BigDecimal.valueOf(100));
        request.setAmountMax(BigDecimal.valueOf(500));
        request.setStatusList(List.of(TransactionStatus.APPROVED));
        request.setCreatedDateFrom(LocalDateTime.now().minusDays(5));
        request.setCreatedDateTo(LocalDateTime.now());

        Transaction transaction = new Transaction();
        FilterTransactionResponse response = new FilterTransactionResponse();

        Mockito.when(transactionRepository.findAll(ArgumentMatchers.<Specification<Transaction>>any())).thenReturn(List.of(transaction));
        Mockito.when(transactionMapper.transactionToFilterTransactionResponse(transaction)).thenReturn(response);

        // When
        List<FilterTransactionResponse> result = transactionService.search(request);

        // Then
        Assertions.assertThat(result).hasSize(1).containsExactly(response);
        Mockito.verify(transactionRepository).findAll(ArgumentMatchers.<Specification<Transaction>>any());
        Mockito.verify(transactionMapper).transactionToFilterTransactionResponse(transaction);
    }

}
