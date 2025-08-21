package com.example.service;

import com.example.entity.Role;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.request.CreateRoleRequest;
import com.example.entity.response.CreateRoleResponse;
import com.example.mapper.RoleMapper;
import com.example.repository.RoleRepository;
import com.example.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RoleServiceImpl.class)
public class RoleServiceImplTest {

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private RoleMapper roleMapper;

    @Autowired
    private RoleServiceImpl roleService;

    @Test
    void createRole() {
        // Input
        CreateRoleRequest request = new CreateRoleRequest();

        // Setup
        Role mappedRole = new Role();
        Role savedRole = new Role();
        savedRole.setActiveStatus(ActiveStatus.ACTIVE);
        CreateRoleResponse expectedResponse = new CreateRoleResponse();

        Mockito.when(roleMapper.createRoleRequestToRole(request)).thenReturn(mappedRole);
        Mockito.when(roleRepository.save(mappedRole)).thenReturn(savedRole);
        Mockito.when(roleMapper.roleToCreateRoleResponse(savedRole)).thenReturn(expectedResponse);

        // Process
        CreateRoleResponse actualResponse = roleService.createRole(request);

        // Output
        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(savedRole.getActiveStatus()).isEqualTo(ActiveStatus.ACTIVE);

        Mockito.verify(roleMapper).createRoleRequestToRole(request);
        Mockito.verify(roleRepository).save(mappedRole);
        Mockito.verify(roleMapper).roleToCreateRoleResponse(savedRole);
    }
}
