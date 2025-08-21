package com.example.entity.response;

import com.example.entity.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private UUID userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private ActiveStatus activeStatus;
    private String createdTime;
    private String createdUser;
}
