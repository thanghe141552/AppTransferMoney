package com.example.mapper;

import com.example.entity.Account;
import com.example.entity.Role;
import com.example.entity.audit.AuditingMetaData;
import com.example.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapperHelper {

    private final RoleRepository roleRepository;

    public UserMapperHelper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role toRole(String roleId) {
        if (roleId == null) return null;
        return roleRepository.findById(UUID.fromString(roleId))
                .orElseThrow(() -> new EntityNotFoundException("Role Id not found: " + roleId));
    }
}
