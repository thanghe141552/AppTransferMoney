package com.example.entity.response;

import com.example.entity.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterAccountResponse {
    private String accountId;
    private String accountName;
    private AccountType accountType;
    private BigDecimal amount;
    private String createdTime;
    private String createdUser;

    private String userId;
    private String accountHolderId;

    private BigDecimal amountSpentInPeriod;
}
