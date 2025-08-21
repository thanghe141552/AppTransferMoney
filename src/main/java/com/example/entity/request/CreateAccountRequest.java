package com.example.entity.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    private String userId;
    private String accountHolderId;
    private String accountName;
    private String password;
    private String accountType;
    private BigDecimal amount;
}
