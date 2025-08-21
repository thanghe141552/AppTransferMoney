package com.example.service.impl;

import com.example.entity.User;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateUserRequest;
import com.example.entity.response.CreateUserResponse;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {


        User userProcessing = userMapper.createUserRequestToUser(createUserRequest);
        userProcessing.setActiveStatus(ActiveStatus.ACTIVE);
        userProcessing = userRepository.save(userProcessing);

        return userMapper.userToCreateUserResponse(userProcessing);
    }

//    @Override
//    public void deleteUser() {
//
//    }
//
//    @Override
//    public User updateUser() {
//        return null;
//    }
//
//    @Override
//    public void getUser() {
//
//    }
//
//    @Override
//    public void getAllUsers() {
//
//    }
}
