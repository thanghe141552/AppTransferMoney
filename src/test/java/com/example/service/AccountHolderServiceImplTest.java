package com.example.service;

import com.example.entity.AccountHolder;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.response.CreateAccountHolderResponse;
import com.example.mapper.AccountHolderMapper;
import com.example.repository.AccountHolderRepository;
import com.example.service.impl.AccountHolderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AccountHolderServiceImpl.class)
public class AccountHolderServiceImplTest {
    @MockitoBean
    private AccountHolderRepository accountHolderRepository;
    @MockitoBean
    private AccountHolderMapper accountHolderMapper;
    @Autowired
    private AccountHolderServiceImpl accountHolderService;

    @Test
    void createAccountHolder() {
        // Input
        CreateAccountHolderRequest request = new CreateAccountHolderRequest();

        // Setup
        AccountHolder mappedEntity = new AccountHolder();
        AccountHolder savedEntity = new AccountHolder();
        savedEntity.setActiveStatus(ActiveStatus.ACTIVE);
        CreateAccountHolderResponse response = new CreateAccountHolderResponse();
        Mockito.when(accountHolderMapper.createAccountHolderRequestToAccountHolder(request)).thenReturn(mappedEntity);
        Mockito.when(accountHolderRepository.save(mappedEntity)).thenReturn(savedEntity);
        Mockito.when(accountHolderMapper.accountHolderToCreateAccountHolderResponse(savedEntity)).thenReturn(response);

        // Process
        CreateAccountHolderResponse actualResponse = accountHolderService.createAccountHolder(request);

        // Output
        assertThat(actualResponse).isEqualTo(response);
        assertThat(savedEntity.getActiveStatus()).isEqualTo(ActiveStatus.ACTIVE);

        Mockito.verify(accountHolderMapper).createAccountHolderRequestToAccountHolder(request);
        Mockito.verify(accountHolderRepository).save(mappedEntity);
        Mockito.verify(accountHolderMapper).accountHolderToCreateAccountHolderResponse(savedEntity);
    }
}