package br.com.finance.ms_transaction.transaction.domain.entities.transaction;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final UUID userId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String currency;
    private final String category;
    private TransactionStatus status;
    private final String description;
    private Instant createdAt;

    public Transaction(
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description
    ) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.description = description;
    }

    public Transaction(
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
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.description = description;
        this.status = TransactionStatus.PENDING;
        this.createdAt = Instant.now();
    }


    public void approve() {
        this.status = TransactionStatus.APPROVED;
    }

    public void reject() {
        this.status = TransactionStatus.REJECTED;
    }

    boolean isExpense() {
        return type == TransactionType.WITHDRAW
                || type == TransactionType.PURCHASE
                || type == TransactionType.TRANSFER;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }
}
