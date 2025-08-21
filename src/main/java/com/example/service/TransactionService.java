package com.example.service;

import com.example.entity.Transaction;
import com.example.entity.projection.TransactionSummary;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import com.example.entity.response.TotalAmountTransactionResponse;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    CreateTransactionResponse createSingleTransaction(CreateTransactionRequest createTransactionRequest);
    CreateTransactionResponse createSingleTransactionWithKafka(CreateTransactionRequest createTransactionRequest);
    List<CreateTransactionResponse> createMultipleTransaction(List<CreateTransactionRequest> createTransactionRequests);
    List<Transaction> getTransactionHistory();
    List<FilterTransactionResponse> getTransactionHistoryByCreatedTime(LocalDateTime createTime);
    TotalAmountTransactionResponse getTotalAmountTransactionByTime(LocalDateTime time);
    Page<FilterTransactionResponse> getTransactionHistoryByAccountId(String accountId, Pageable pageable);
    Window<FilterTransactionResponse> getTransactionHistoryByAccountId(String accountId, ScrollPosition scrollPosition);
    List<FilterTransactionResponse> search(FilterTransactionRequest filterTransactionRequest);
    List<TransactionSummary> getListTransactionTransfer(String fromAccountId);
}
