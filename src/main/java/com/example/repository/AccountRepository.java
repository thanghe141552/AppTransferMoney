package com.example.repository;

import com.example.entity.Account;
import com.example.entity.enums.AccountType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, AccountRepositoryCustom {
    @EntityGraph(attributePaths = {"user", "user.role"})
    Optional<Account> findAccountById(UUID id);

    @EntityGraph(attributePaths = {"user", "accountHolder"})
    List<Account> findAccountsByAccountType(AccountType accountType);

    @EntityGraph(attributePaths = {"user", "user.role"})
    Optional<Account> findAccountByAccountName(String accountName);
}
