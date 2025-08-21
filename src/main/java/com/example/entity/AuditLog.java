package com.example.entity;

import com.example.entity.audit.AuditingMetaData;
import com.example.entity.enums.AuditAction;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.Type;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AuditLog extends AuditingMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "audit_id", nullable = false)
    private UUID id;

    @Column(name = "action", nullable = false)
    private AuditAction action;

    @Type(value = JsonBinaryType.class)
    @Column(name = "data", nullable = false, columnDefinition = "JSONB")
    private String data;
}
