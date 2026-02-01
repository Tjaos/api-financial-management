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
    private final String description;
    private final Instant createdAt;
    private TransactionStatus status;

    public static Transaction create(
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description
    ) {
        return new Transaction(
                UUID.randomUUID(),
                userId,
                type,
                amount,
                currency,
                category,
                description,
                TransactionStatus.PENDING,
                Instant.now()
        );
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
        if(id == null) throw new IllegalArgumentException("O id da transação é obrigatório.");
        if(userId == null) throw new IllegalArgumentException("O id do usuário é obrigatório");
        if(type == null) throw new IllegalArgumentException("O tipo da transação é obrigatório.");
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        if (currency == null || currency.isBlank()) currency = "BRL";
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Categoria é obrigatória");
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


    public void approve() {
        if(!isPending()){
            throw new IllegalStateException("A transação não está pendente e não pode ser aprovada.");
        }
        this.status = TransactionStatus.APPROVED;
    }

    public void reject() {
        if(!isPending()){
            throw new IllegalStateException("A transação não está pendente e não pode ser rejeitada.");
        }
        this.status = TransactionStatus.REJECTED;
    }

    boolean isExpense() {
        return type == TransactionType.WITHDRAW
                || type == TransactionType.PURCHASE
                || type == TransactionType.TRANSFER;
    }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
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

}
