package com.example.mapper;

import com.example.entity.User;
import com.example.entity.request.CreateUserRequest;
import com.example.entity.response.CreateUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapperHelper.class})
public interface UserMapper {
    @Mapping(target = "userId", source = "id")
    CreateUserResponse userToCreateUserResponse(User user);

    @Mappings({
            @Mapping(target = "activeStatus", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "role", source = "roleId"),
            @Mapping(target = "accounts", ignore = true)
    })
    User createUserRequestToUser(CreateUserRequest createUserRequest);
}
