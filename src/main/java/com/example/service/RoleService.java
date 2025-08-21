package com.example.service;

import com.example.entity.request.CreateRoleRequest;
import com.example.entity.response.CreateRoleResponse;

public interface RoleService {
    CreateRoleResponse createRole(CreateRoleRequest createRoleRequest);
}
