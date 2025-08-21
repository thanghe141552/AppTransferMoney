package com.example.repository.spec;

import com.example.entity.Transaction;
import com.example.entity.enums.TransactionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public class TransactionSpecification {
    public static Specification<Transaction> hasStatus(TransactionStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("transactionStatus"), status);
    }

    public static Specification<Transaction> hasAmountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<Transaction> hasAmountLessThanOrEqual(BigDecimal maxAmount) {
        return (root, query, cb) -> maxAmount == null ? null : cb.lessThanOrEqualTo(root.get("amount"), maxAmount);
    }

    public static Specification<Transaction> createdAfter(LocalDateTime fromCreatedDate) {
        return (root, query, cb) -> fromCreatedDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdTime"), fromCreatedDate);
    }

    public static Specification<Transaction> createdBefore(LocalDateTime toCreatedDate) {
        return (root, query, cb) -> toCreatedDate == null ? null : cb.lessThanOrEqualTo(root.get("createdTime"), toCreatedDate);
    }
}
