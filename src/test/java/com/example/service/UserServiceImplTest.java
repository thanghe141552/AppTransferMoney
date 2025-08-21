package com.example.service;

import com.example.entity.AccountHolder;
import com.example.entity.User;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateAccountHolderRequest;
import com.example.entity.request.CreateUserRequest;
import com.example.entity.response.CreateAccountHolderResponse;
import com.example.entity.response.CreateUserResponse;
import com.example.mapper.AccountHolderMapper;
import com.example.mapper.UserMapper;
import com.example.repository.AccountHolderRepository;
import com.example.repository.UserRepository;
import com.example.service.impl.AccountHolderServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserServiceImpl.class)
public class UserServiceImplTest {
    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void createUser() {
        // Input
        CreateUserRequest request = new CreateUserRequest();

        // Setup
        User mappedUser = new User();
        User savedUser = new User();
        savedUser.setActiveStatus(ActiveStatus.ACTIVE);
        CreateUserResponse expectedResponse = new CreateUserResponse();

        Mockito.when(userMapper.createUserRequestToUser(request)).thenReturn(mappedUser);
        Mockito.when(userRepository.save(mappedUser)).thenReturn(savedUser);
        Mockito.when(userMapper.userToCreateUserResponse(savedUser)).thenReturn(expectedResponse);

        // Process
        CreateUserResponse actualResponse = userService.createUser(request);

        // Output
        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(savedUser.getActiveStatus()).isEqualTo(ActiveStatus.ACTIVE);

        Mockito.verify(userMapper).createUserRequestToUser(request);
        Mockito.verify(userRepository).save(mappedUser);
        Mockito.verify(userMapper).userToCreateUserResponse(savedUser);
    }
}