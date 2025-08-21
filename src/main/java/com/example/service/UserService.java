package com.example.service;

import com.example.entity.User;
import com.example.entity.request.CreateUserRequest;
import com.example.entity.response.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest userRequest);
//
//    void deleteUser();
//
//    User updateUser();
//
//    void getUser();
//
//    void getAllUsers();
}
