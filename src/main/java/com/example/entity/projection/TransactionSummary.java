package com.example.entity.projection;

import com.example.entity.Account;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface TransactionSummary {
    String getId();

    BigDecimal getAmount();

    CreateTransactionAccount getToAccount();

    Instant getCreatedTime();

    interface CreateTransactionAccount {
        String getId();
    }
}
