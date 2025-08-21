package com.example.proxy;

import com.example.cache.TransactionCache;
import com.example.entity.Transaction;
import com.example.entity.projection.TransactionSummary;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import com.example.entity.response.TotalAmountTransactionResponse;
import com.example.mapper.TransactionMapper;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Component
public class TransactionServiceProxy implements TransactionService {

    private final TransactionService transactionService;

    private final TransactionCache transactionCache;

    private final TransactionMapper transactionMapper;

    public TransactionServiceProxy(@Qualifier("transactionServiceImpl") TransactionService transactionService, TransactionCache transactionCache, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionCache = transactionCache;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public CreateTransactionResponse createSingleTransaction(CreateTransactionRequest createTransactionRequest) {
        CreateTransactionResponse transactionResponse = transactionService.createSingleTransaction(createTransactionRequest);
        transactionCache.addTransaction(transactionMapper.createTransactionResponseToTransaction(transactionResponse));
        return transactionResponse;
    }

    @Override
    public CreateTransactionResponse createSingleTransactionWithKafka(CreateTransactionRequest createTransactionRequest) {
        return null;
    }

    @Override
    public List<CreateTransactionResponse> createMultipleTransaction(List<CreateTransactionRequest> createTransactionRequests) {
        List<CreateTransactionResponse> transactionResponses = transactionService.createMultipleTransaction(createTransactionRequests);
        transactionResponses.forEach(transactionResponse -> {transactionCache.addTransaction(transactionMapper.createTransactionResponseToTransaction(transactionResponse));});
        return transactionResponses;
    }

    @Override
    public List<Transaction> getTransactionHistory() {
        return transactionService.getTransactionHistory();
    }

    @Override
    public List<FilterTransactionResponse> getTransactionHistoryByCreatedTime(LocalDateTime createTime) {
        return List.of();
    }

    @Override
    public TotalAmountTransactionResponse getTotalAmountTransactionByTime(LocalDateTime time) {
        return null;
    }

    @Override
    public Page<FilterTransactionResponse> getTransactionHistoryByAccountId(String accountId, Pageable pageable) {
        return null;
    }

    @Override
    public Window<FilterTransactionResponse> getTransactionHistoryByAccountId(String accountId, ScrollPosition scrollPosition) {
        return null;
    }

    @Override
    public List<FilterTransactionResponse> search(FilterTransactionRequest filterTransactionRequest) {
        return List.of();
    }

    @Override
    public List<TransactionSummary> getListTransactionTransfer(String fromAccountId) {
        return List.of();
    }
}
