package com.example.service.impl;

import com.example.entity.Role;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateRoleRequest;
import com.example.entity.response.CreateRoleResponse;
import com.example.mapper.RoleMapper;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public CreateRoleResponse createRole(CreateRoleRequest createRoleRequest) {
        Role roleProcessing = roleMapper.createRoleRequestToRole(createRoleRequest);
        roleProcessing.setActiveStatus(ActiveStatus.ACTIVE);
        roleProcessing = roleRepository.save(roleProcessing);
        return roleMapper.roleToCreateRoleResponse(roleProcessing);
    }
}
