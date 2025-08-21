package com.example.mapper;

import com.example.entity.Account;
import com.example.entity.Transaction;
import com.example.entity.audit.AuditingMetaData;
import com.example.entity.enums.ActiveStatus;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionMapperHelper {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    public TransactionMapperHelper(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Named("toTransaction")
    public Transaction toTransaction(String transactionId) {
        if (transactionId == null) return null;
        return transactionRepository.findById(UUID.fromString(transactionId))
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + transactionId));
    }

    @Named("toAccount")
    public Account toAccount(String accountId) {
        if (accountId == null) return null;
        return accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new EntityNotFoundException("Account Id not found: " + accountId));
    }

    @Named("toAccountId")
    public String toAccountId(Account account) {
        return account.getId().toString();
    }

    @Named("toAccountStatus")
    public ActiveStatus toAccountStatus(Account account) {
        return account.getActiveStatus();
    }

    @Named("toAccountName")
    public String toAccountName(Account account) {
        return account.getAccountName();
    }
}
