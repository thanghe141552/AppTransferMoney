package com.example.builder;

import com.example.entity.Account;
import com.example.entity.Transaction;
import com.example.entity.audit.AuditingMetaData;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.enums.TransactionStatus;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
@Lazy
public class TransactionBuilder {

    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;

    public TransactionBuilder fromAccountId(Account fromAccount) {
        this.fromAccount = fromAccount;
        return this;
    }

    public TransactionBuilder sendToAccountId(Account toAccount) {
        this.toAccount = toAccount;
        return this;
    }

    public TransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Transaction build(){
        return new Transaction(UUID.randomUUID(), fromAccount, toAccount, amount, TransactionStatus.PENDING, ActiveStatus.ACTIVE);
    }
}
