package com.example.entity;

import com.example.entity.audit.AuditingMetaData;
import com.example.entity.enums.AccountType;
import com.example.entity.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"transactionsAsFromAccount", "transactionsAsToAccount"})
public class Account extends AuditingMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, name = "account_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_holder_id", nullable = false)
    private AccountHolder accountHolder;

    @Column(name = "active_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionsAsFromAccount = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionsAsToAccount = new ArrayList<>();
}
