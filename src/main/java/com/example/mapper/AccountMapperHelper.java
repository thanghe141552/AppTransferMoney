package com.example.mapper;

import com.example.entity.Account;
import com.example.entity.AccountHolder;
import com.example.entity.User;
import com.example.entity.audit.AuditingMetaData;
import com.example.entity.enums.ActiveStatus;
import com.example.repository.AccountHolderRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountMapperHelper {

    private final UserRepository userRepository;
    private final AccountHolderRepository accountHolderRepository;

    public AccountMapperHelper(UserRepository userRepository, AccountHolderRepository accountHolderRepository) {
        this.userRepository = userRepository;
        this.accountHolderRepository = accountHolderRepository;
    }

    public User toUser(String userId) {
        if (userId == null) return null;
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    public AccountHolder toAccountHolder(String accountHolderId) {
        if (accountHolderId == null) return null;
        return accountHolderRepository.findById(UUID.fromString(accountHolderId))
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + accountHolderId));
    }

    public String toUserId(User user) {
        return user.getId().toString();
    }

    public String toAccountHolderId(AccountHolder accountHolder) {
        return accountHolder.getId().toString();
    }
}
