package com.example.service;

import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.response.CreateAccountHolderResponse;

public interface AccountHolderService {
    CreateAccountHolderResponse createAccountHolder(CreateAccountHolderRequest createAccountHolderRequest);
}
