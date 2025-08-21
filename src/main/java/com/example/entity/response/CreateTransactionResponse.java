package com.example.entity.response;

import com.example.entity.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionResponse implements Serializable {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private String amount;
    private String failMessage;
    private TransactionStatus transactionStatus;
    private String createdTime;
    private String createdUser;
}
