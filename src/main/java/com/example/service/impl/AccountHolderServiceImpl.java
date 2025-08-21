package com.example.service.impl;

import com.example.entity.AccountHolder;
import com.example.entity.Transaction;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.enums.TransactionStatus;
import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.response.CreateAccountHolderResponse;
import com.example.mapper.AccountHolderMapper;
import com.example.mapper.TransactionMapper;
import com.example.repository.AccountHolderRepository;
import com.example.repository.TransactionRepository;
import com.example.service.AccountHolderService;
import org.springframework.stereotype.Service;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;
    private final AccountHolderMapper accountHolderMapper;

    public AccountHolderServiceImpl(AccountHolderRepository accountHolderRepository, AccountHolderMapper accountHolderMapper) {
        this.accountHolderRepository = accountHolderRepository;
        this.accountHolderMapper = accountHolderMapper;
    }


    @Override
    public CreateAccountHolderResponse createAccountHolder(CreateAccountHolderRequest createAccountHolderRequest) {
        AccountHolder accountHolderProcessing = accountHolderMapper.createAccountHolderRequestToAccountHolder(createAccountHolderRequest) ;
        accountHolderProcessing.setActiveStatus(ActiveStatus.ACTIVE);
        accountHolderProcessing = accountHolderRepository.save(accountHolderProcessing);
        return accountHolderMapper.accountHolderToCreateAccountHolderResponse(accountHolderProcessing);
    }
}
