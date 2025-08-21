package com.example.repository;

import com.example.entity.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, UUID> {
}
