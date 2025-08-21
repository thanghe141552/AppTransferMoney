package com.example.repository.impl;

import com.example.entity.response.FilterAccountResponse;
import com.example.repository.AccountRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String defaultSchema;

    @Override
    @SuppressWarnings("unchecked")
    public List<FilterAccountResponse> getAmountSpentInMonth(List<String> accountIds, LocalDateTime timeStart) {

        LocalDateTime timeEnd = timeStart.plus(Duration.ofDays(30));

        String sql = String.format("""
                     SELECT\s
                         t.from_account_id as accountId,\s
                         a.account_name as accountName,\s
                         a.amount as amount,\s
                         SUM(t.amount) AS amountSpentInMonth,\s
                         EXTRACT(YEAR FROM t.created_time) AS transaction_year,\s
                         EXTRACT(MONTH FROM t.created_time) AS transaction_year\s
                     FROM\s
                         %1$s.transaction t inner join %1$s.account a on t.from_account_id=a.account_id\s
                     WHERE\s
                         t.from_account_id IN (:accountIds)\s
                         AND t.created_time BETWEEN :start AND :end\s
                         AND t.transaction_status = 'APPROVED'\s
                     GROUP BY\s
                        t.from_account_id,\s
                        a.account_name,\s
                        a.amount,\s
                        EXTRACT(YEAR FROM t.created_time),\s
                        EXTRACT(MONTH FROM t.created_time)\s
                    ORDER BY\s
                        5,\s
                        6;\s
                \s""", defaultSchema);
        return (List<FilterAccountResponse>) entityManager.createNativeQuery(sql)
                .setParameter("accountIds", accountIds.stream().map(UUID::fromString).toList())
                .setParameter("start", timeStart)
                .setParameter("end", timeEnd)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("accountId")
                .addScalar("accountName")
                .addScalar("amount")
                .addScalar("amountSpentInMonth")
                .setTupleTransformer((tuple, aliases) -> FilterAccountResponse.builder().accountId(tuple[0].toString())
                        .accountName(tuple[1].toString()).amount(new BigDecimal(tuple[2].toString()))
                        .amountSpentInPeriod(new BigDecimal(tuple[3].toString())).build())
                .getResultList();
    }

//    CSVFileHelper<Transaction> csvFileHelper;
//
//    public TransactionRepositoryImpl (){
//        csvFileHelper = new CSVFileHelper<>();
//    }
//
//    public void saveTransaction(Transaction transaction) {
//        csvFileHelper.writeToFile(AppConstant.FILE_TRANSACTION_URL, List.of(transaction));
//    }
//
//    public List<Transaction> readTransactions() {
//        return csvFileHelper.readFromFile(AppConstant.FILE_TRANSACTION_URL, new Transaction());
//    }
}
