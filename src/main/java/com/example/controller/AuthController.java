package com.example.controller;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login/jwt")
    public Map<String, Object> loginJwt(@RequestBody LoginRequest loginRequest) {
        Account account = accountRepository.findAccountByAccountName(loginRequest.getAccountName())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword()))
            throw new AuthenticationException("Wrong password") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        String token = JwtUtils.generateToken(String.valueOf(account.getId()), String.valueOf(account.getUser().getRole().getAuthority()));
        return Map.of("accessToken", token);
    }

    @Getter
    @Setter
    public static class LoginRequest {
        private String accountName;
        private String password;
    }
}
