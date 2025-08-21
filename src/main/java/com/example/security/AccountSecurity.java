package com.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountSecurity {
    public boolean isAccountOwner(String requestId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentAccountId = auth.getPrincipal().toString();
        return currentAccountId.equals(requestId);
    }
}
