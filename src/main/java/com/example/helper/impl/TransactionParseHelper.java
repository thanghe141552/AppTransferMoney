//package com.example.helper.impl;
//
//import com.example.entity.Account;
//import com.example.entity.Transaction;
//import com.example.helper.EntityParseHelper;
//import com.example.entity.enums.TransactionStatus;
//import com.example.repository.AccountRepository;
//import com.example.repository.TransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.UUID;
//
//@Component
//@Lazy
//public class TransactionParseHelper implements EntityParseHelper<Transaction> {
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    //    UUID transactionId, int fromAccountId, int toAccountId, BigDecimal amount, Instant createdTime, Instant updateTime, TransactionStatus activeStatus
//    @Override
//    public Transaction parse(String data) {
//        String[] fields = data.split(",");
//        String transactionId = fields[0].trim();
//        Account fromAccount = accountRepository.findById(UUID.fromString(fields[1].trim())).orElse(null);
//        Account toAccount = accountRepository.findById(UUID.fromString(fields[2].trim())).orElse(null);
//        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(fields[3].trim()));
//        Instant createdTime = Instant.parse(fields[4].trim());
//        Instant updateTime = Instant.parse(fields[5].trim());
//        TransactionStatus activeStatus = TransactionStatus.fromValue(Integer.parseInt(fields[6].trim()));
//        return new Transaction(UUID.fromString(transactionId), fromAccount, toAccount, amount, createdTime, updateTime, activeStatus);
//    }
//}
