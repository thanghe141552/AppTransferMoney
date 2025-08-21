package com.example.controller;

import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.request.CreateUserRequest;
import com.example.entity.response.CreateAccountHolderResponse;
import com.example.entity.response.CreateUserResponse;
import com.example.service.AccountHolderService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-holder")
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    public AccountHolderController(AccountHolderService accountHolderService) {
        this.accountHolderService = accountHolderService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<CreateAccountHolderResponse> createAccountHolder(@RequestBody CreateAccountHolderRequest createAccountHolderRequest){
        CreateAccountHolderResponse accountHolderResponse = accountHolderService.createAccountHolder(createAccountHolderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountHolderResponse);
    }
}
