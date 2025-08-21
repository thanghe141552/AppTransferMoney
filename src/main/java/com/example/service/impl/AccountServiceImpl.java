package com.example.service.impl;

import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.enums.AccountType;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.request.FilterAccountRequest;
import com.example.entity.request.FilterTransactionRequest;
import com.example.entity.response.CreateAccountResponse;
import com.example.entity.response.FilterAccountResponse;
import com.example.mapper.AccountMapper;
import com.example.repository.AccountRepository;
import com.example.repository.UserRepository;
import com.example.service.AccountService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.userRepository = userRepository;
    }


    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        Account accountProcessing = accountMapper.createAccountRequestToAccount(createAccountRequest);
        accountProcessing.setActiveStatus(ActiveStatus.ACTIVE);
        accountProcessing = accountRepository.save(accountProcessing);
        return accountMapper.accountToCreateAccountResponse(accountProcessing);
    }

    @Override
    public List<FilterAccountResponse> getAccountByAccountType(AccountType accountType) {
        List<Account> accountList = accountRepository.findAccountsByAccountType(accountType);
        return accountList.stream().map(accountMapper::accountToFilterAccountResponse).toList();
    }

    @Override
    public List<FilterAccountResponse> getAccountByAccountTypeAndUserId(AccountType accountType, String userId) {

        User userFilter = null;
        if (userId != null){
            userFilter = userRepository.findUserById(UUID.fromString(userId));
        }


        Account probe = new Account();
        probe.setAccountType(accountType);
        probe.setUser(userFilter);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues();

        if (accountType != null){
            matcher.withMatcher("accountType", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        if (userFilter != null){
            matcher.withMatcher("user", ExampleMatcher.GenericPropertyMatchers.exact());
        }

        Example<Account> example = Example.of(probe, matcher);
        List<Account> accountList = accountRepository.findAll(example);

        return accountList.stream().map(accountMapper::accountToFilterAccountResponse).toList();
    }

    @Override
    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public BigDecimal getAmountByAccountId(String accountId) {
        if(accountId == null || accountId.isEmpty()){
            return null;
        }
        Account account = accountRepository.findAccountById(UUID.fromString(accountId)).orElse(null);
        return account == null ? null : account.getAmount();
    }

    @Transactional(readOnly = true, transactionManager = "jpaTransactionManager")
    public List<FilterAccountResponse> getAmountSpentInMonth(FilterAccountRequest filterAccountRequest) {
        return accountRepository.getAmountSpentInMonth(filterAccountRequest.getAccountIds(), filterAccountRequest.getDateFrom());
    }

}
