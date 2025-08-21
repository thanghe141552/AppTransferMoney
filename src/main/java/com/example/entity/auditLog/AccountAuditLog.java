package com.example.entity.auditLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountAuditLog {
    private String accountId;
    private String userId;
    private String accountHolderId;
    private String accountName;
    private String accountType;
    private BigDecimal currentAmount;
    private BigDecimal amountTransferred;
    private BigDecimal amountReceived;
}
