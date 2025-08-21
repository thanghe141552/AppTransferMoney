package com.example.repository;

import com.example.entity.response.FilterAccountResponse;
import com.example.entity.response.FilterTransactionResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountRepositoryCustom {
    List<FilterAccountResponse> getAmountSpentInMonth(List<String> accountIds, LocalDateTime time);
}
