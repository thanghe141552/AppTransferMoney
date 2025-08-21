package com.example.service;

import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.enums.AccountType;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateAccountRequest;
import com.example.entity.request.FilterAccountRequest;
import com.example.entity.response.CreateAccountResponse;
import com.example.entity.response.FilterAccountResponse;
import com.example.mapper.AccountMapper;
import com.example.repository.AccountRepository;
import com.example.repository.UserRepository;
import com.example.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AccountServiceImpl.class)
public class AccountServiceImplTest {

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private AccountMapper accountMapper;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private AccountServiceImpl accountService;

    @Test
    void createAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        Account mappedAccount = new Account();
        Account savedAccount = new Account();
        savedAccount.setActiveStatus(ActiveStatus.ACTIVE);
        CreateAccountResponse expectedResponse = new CreateAccountResponse();

        Mockito.when(accountMapper.createAccountRequestToAccount(request)).thenReturn(mappedAccount);
        Mockito.when(accountRepository.save(mappedAccount)).thenReturn(savedAccount);
        Mockito.when(accountMapper.accountToCreateAccountResponse(savedAccount)).thenReturn(expectedResponse);

        CreateAccountResponse actualResponse = accountService.createAccount(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(savedAccount.getActiveStatus()).isEqualTo(ActiveStatus.ACTIVE);

        Mockito.verify(accountMapper).createAccountRequestToAccount(request);
        Mockito.verify(accountRepository).save(mappedAccount);
        Mockito.verify(accountMapper).accountToCreateAccountResponse(savedAccount);
    }

    @Test
    void getAccountByAccountType() {
        AccountType type = AccountType.SINGLE_ACCOUNT;
        Account account = new Account();
        FilterAccountResponse response = new FilterAccountResponse();

        Mockito.when(accountRepository.findAccountsByAccountType(type)).thenReturn(List.of(account));
        Mockito.when(accountMapper.accountToFilterAccountResponse(account)).thenReturn(response);

        List<FilterAccountResponse> result = accountService.getAccountByAccountType(type);

        assertThat(result).containsExactly(response);
    }

    @Test
    void getAccountByAccountTypeAndUserId() {
        AccountType type = AccountType.SINGLE_ACCOUNT;
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Account account = new Account();
        account.setUser(user);
        account.setAccountType(type);

        FilterAccountResponse response = new FilterAccountResponse();

        Mockito.when(userRepository.findUserById(userId)).thenReturn(user);
        Mockito.when(accountRepository.findAll(ArgumentMatchers.<Example<Account>>any())).thenReturn(List.of(account));
        Mockito.when(accountMapper.accountToFilterAccountResponse(account)).thenReturn(response);

        List<FilterAccountResponse> result = accountService.getAccountByAccountTypeAndUserId(type, userId.toString());

        assertThat(result).containsExactly(response);
    }

    @Test
    void getAmountByAccountId_returnAmount() {
        UUID accountId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.TEN;
        Account account = new Account();
        account.setAmount(amount);

        Mockito.when(accountRepository.findAccountById(accountId)).thenReturn(Optional.of(account));

        BigDecimal result = accountService.getAmountByAccountId(accountId.toString());

        assertThat(result).isEqualTo(amount);
    }

    @Test
    void getAmountByAccountId_returnNullWhenNotFound() {
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountRepository.findAccountById(accountId)).thenReturn(Optional.empty());

        BigDecimal result = accountService.getAmountByAccountId(accountId.toString());

        assertThat(result).isNull();
    }

    @Test
    void getAmountSpentInMonth() {
        FilterAccountRequest filterRequest = new FilterAccountRequest();
        filterRequest.setAccountIds(List.of(UUID.randomUUID().toString()));
        filterRequest.setDateFrom(LocalDateTime.now());

        FilterAccountResponse response = new FilterAccountResponse();
        Mockito.when(accountRepository.getAmountSpentInMonth(filterRequest.getAccountIds(), filterRequest.getDateFrom())).thenReturn(List.of(response));

        List<FilterAccountResponse> result = accountService.getAmountSpentInMonth(filterRequest);

        assertThat(result).containsExactly(response);
    }
}
