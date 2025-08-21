package com.example.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountHolderResponse {
    private String accountHolderId;
    private String name;
    private String address;
    private String activeStatus;
    private String createdTime;
    private String createdUser;
}
