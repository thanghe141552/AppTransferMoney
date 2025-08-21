package com.example.entity;

import com.example.entity.audit.AuditingMetaData;
import com.example.entity.enums.ActiveStatus;
import com.example.entity.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction extends AuditingMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, name = "transaction_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;


    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "active_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;
}
