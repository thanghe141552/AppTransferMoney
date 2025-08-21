package com.example.controller;

import com.example.config.instant.InstantDateOnlyDeserializer;
import com.example.config.instant.InstantDateOnlySerializer;
import com.example.entity.Account;
import com.example.entity.enums.AccountType;
import com.example.entity.projection.TransactionSummary;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.request.FilterAccountRequest;
import com.example.entity.request.FilterScrollingTransactionRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateAccountResponse;
import com.example.entity.response.FilterAccountResponse;
import com.example.entity.response.FilterScrollingTransactionResponse;
import com.example.entity.response.FilterTransactionResponse;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;

    public AccountController(AccountService accountService, @Qualifier("transactionServiceImpl") TransactionService transactionService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        createAccountRequest.setPassword(passwordEncoder.encode(createAccountRequest.getPassword()));
        CreateAccountResponse accountResponse = accountService.createAccount(createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @GetMapping("/{accountId}/transactions")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isAccountOwner(#accountId)")
    public ResponseEntity<Page<FilterTransactionResponse>> getAccountTransactionsByPage(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<FilterTransactionResponse> transactions = transactionService.getTransactionHistoryByAccountId(accountId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{accountId}/transactions/scroll")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isAccountOwner(#accountId)")
    public ResponseEntity<FilterScrollingTransactionResponse> getAccountTransactionsByScroll(
            @PathVariable String accountId,
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTime
    ) {
        Map<String, ?> keys = (id == null)
                ? Collections.emptyMap()
                : Map.of("id", id, "createdTime", createdTime);

        ScrollPosition scrollPosition =
                (id == null)
                        ? ScrollPosition.keyset()
                        : ScrollPosition.forward(keys);

        Window<FilterTransactionResponse> transactionResponses = transactionService.getTransactionHistoryByAccountId(
                accountId, scrollPosition);

        return ResponseEntity.ok(new FilterScrollingTransactionResponse(
                transactionResponses.getContent(),
                transactionResponses.hasNext(),
                (KeysetScrollPosition) transactionResponses.positionAt(transactionResponses.getContent().size() - 1) // to be sent back in next request
        ));
    }


    @GetMapping("/{accountId}/getTransactionTransfer")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isAccountOwner(#accountId)")
    public ResponseEntity<List<TransactionSummary>> getListTransactionTransfer(@PathVariable String accountId) {
        List<TransactionSummary> transactionSummaries = transactionService.getListTransactionTransfer(accountId);
        return ResponseEntity.ok(transactionSummaries);
    }

    @GetMapping("/getByAccountType")
    public ResponseEntity<List<FilterAccountResponse>> getAccountByAccountType(@RequestParam AccountType accountType) {
        List<FilterAccountResponse> accountResponses = accountService.getAccountByAccountType(accountType);
        return ResponseEntity.ok(accountResponses);
    }

    @GetMapping("/{accountId}/amount")
    public ResponseEntity<BigDecimal> getAccountAmount(@PathVariable String accountId) {
        BigDecimal accountAmount = accountService.getAmountByAccountId(accountId);
        return ResponseEntity.ok(accountAmount);
    }

    @GetMapping("/getByAccountTypeAndUser")
    public ResponseEntity<List<FilterAccountResponse>> getAccountByAccountTypeAndUser(
            @RequestParam(required = false) AccountType accountType,
            @RequestParam(required = false) String userId) {
        List<FilterAccountResponse> accountResponses = accountService.getAccountByAccountTypeAndUserId(accountType, userId);
        return ResponseEntity.ok(accountResponses);
    }

    @PostMapping("/amountPerMonth")
    public ResponseEntity<List<FilterAccountResponse>> getAmountSpentInMonth(@RequestBody FilterAccountRequest filterAccountRequest) {
        List<FilterAccountResponse> filterAccountResponses = accountService.getAmountSpentInMonth(filterAccountRequest);
        return ResponseEntity.ok(filterAccountResponses);
    }
}
