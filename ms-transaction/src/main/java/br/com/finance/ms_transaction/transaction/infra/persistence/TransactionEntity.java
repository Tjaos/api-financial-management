package br.com.finance.ms_transaction.transaction.infra.persistence;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus status;

    @Column(nullable = false)
    private String currency;

    @Column(length = 500)
    private String description;

    private Instant createdAt;


    public TransactionEntity() {

    }

    public TransactionEntity(
            UUID id,
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description,
            TransactionStatus status,
            Instant createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }


    public TransactionEntity(UUID id, UUID userId, TransactionType type, BigDecimal amount, String currency, String category, TransactionStatus status, Instant createdAt) {
    }
}
