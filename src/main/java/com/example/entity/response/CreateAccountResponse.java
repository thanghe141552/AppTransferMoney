package com.example.entity.response;

import com.example.entity.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountResponse {
    private String accountId;
    private String userId;
    private String accountHolderId;
    private String accountName;
    private String password;
    private String accountType;
    private BigDecimal amount;
    private String createdTime;
    private String createdUser;
}
