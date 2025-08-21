package com.example.security;

import com.example.entity.Account;
import com.example.entity.User;
import com.example.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public CustomUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findAccountById(UUID.fromString(username))
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        User user = account.getUser();

        return org.springframework.security.core.userdetails.User.withUsername(account.getId().toString())
                .password(account.getPassword())
                .roles(String.valueOf(user.getRole()))
                .build();
    }
}
