package com.example.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFileErrResponse implements Serializable {
    private String transactionId;
    String errorLine;
    private String failMessage;
}
