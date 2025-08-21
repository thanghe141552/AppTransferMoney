package com.example.mapper;

import com.example.entity.Role;
import com.example.entity.request.CreateRoleRequest;
import com.example.entity.response.CreateRoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {RoleMapperHelper.class})
public interface RoleMapper {
    @Mapping(target = "roleId", source = "id")
    CreateRoleResponse roleToCreateRoleResponse(Role role);

    @Mappings({
            @Mapping(target = "activeStatus", ignore = true),
            @Mapping(target = "users", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Role createRoleRequestToRole(CreateRoleRequest createRoleRequest);
}
