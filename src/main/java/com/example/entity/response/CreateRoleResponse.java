package com.example.entity.response;

import com.example.entity.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleResponse {
    private String roleId;
    private String roleName;
    private String activeStatus;
    private String createdTime;
    private String createdUser;
}
