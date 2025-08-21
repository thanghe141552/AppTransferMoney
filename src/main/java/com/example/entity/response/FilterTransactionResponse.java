package com.example.entity.response;

import com.example.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterTransactionResponse {
    private UUID id;
    private BigDecimal amount;
    private String fromAccountId;
    private String fromAccountName;
    private String fromAccountStatus;
    private String toAccountId;
    private String toAccountName;
    private String toAccountStatus;
    private String transactionStatus;
    private String createdTime;

//    @PersistenceCreator
//    public FilterTransactionResponse(UUID id, BigDecimal amount, Account fromAccount, Account toAccount, Instant createdTime) {
//        this.id = id;
//        this.amount = amount;
//        this.fromAccountId = fromAccount.getId().toString();
//        this.toAccountId = toAccount.getId().toString();
//        this.createdTime = createdTime.toString();
//    }
//
//    @Data
//    public static class CreateTransactionAccountDto {
//        private String accountId;
//
//        public CreateTransactionAccountDto(String accountId) {
//            this.accountId = accountId;
//        }
//    }
}
