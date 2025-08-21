package com.example.repository;

import com.example.entity.Account;
import com.example.entity.Transaction;
import com.example.entity.projection.TransactionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"})
    List<Transaction> findByCreatedTimeAfter(LocalDateTime createdTime);

    @Query(value = "SELECT SUM(amount) AS total_amount\n" +
            "FROM transaction\n" +
            "WHERE transaction_status = 'APPROVED' " +
            "AND created_time >= :timeStart\n" +
            "AND created_time < :timeEnd", nativeQuery = true)
    BigDecimal findAmountTransferByTime(@Param("timeStart") LocalDateTime timeStart, @Param("timeEnd") LocalDateTime timeEnd);

    //Get transaction from a specific account
    @EntityGraph(attributePaths = {"fromAccount", "toAccount"})
    Page<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount, Pageable pageable);

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"})
    Window<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount, ScrollPosition position);

    List<TransactionSummary>  findByFromAccount(Account fromAccount);

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"})
    Window<Transaction> findFirst3ByFromAccountOrToAccountOrderByCreatedTime(Account fromAccount, Account toAccount, ScrollPosition position);
}
