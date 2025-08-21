package com.example.service;

import com.example.entity.enums.AccountType;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.request.FilterAccountRequest;
import com.example.entity.response.CreateAccountResponse;
import com.example.entity.response.FilterAccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);
    List<FilterAccountResponse> getAccountByAccountType(AccountType accountType);
    List<FilterAccountResponse> getAccountByAccountTypeAndUserId(AccountType accountType, String userId);
    BigDecimal getAmountByAccountId(String accountId);
    List<FilterAccountResponse> getAmountSpentInMonth(FilterAccountRequest filterAccountRequest);
}
