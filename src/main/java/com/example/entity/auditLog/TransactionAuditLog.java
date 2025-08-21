package com.example.entity.auditLog;

import com.example.entity.Transaction;
import com.example.entity.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAuditLog {
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;
}
