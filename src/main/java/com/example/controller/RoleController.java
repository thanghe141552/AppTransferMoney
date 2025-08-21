package com.example.controller;

import com.example.entity.request.CreateRoleRequest;
import com.example.entity.request.CreateTransactionRequest;
import com.example.entity.response.CreateRoleResponse;
import com.example.entity.response.CreateTransactionResponse;
import com.example.service.RoleService;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @PostMapping("/create")
    public ResponseEntity<CreateRoleResponse> createRole(@RequestBody CreateRoleRequest createRoleRequest) {
        CreateRoleResponse roleResponse = roleService.createRole(createRoleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleResponse);
    }
}
