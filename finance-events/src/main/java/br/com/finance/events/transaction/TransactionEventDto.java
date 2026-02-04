package br.com.finance.events.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransactionEventDto {

    private UUID transactionId;
    private UUID userId;
    private BigDecimal amount;
    private String type;    // DEPOSIT / WITHDRAW / TRANSFER / PURCHASE
    private String status;  // PENDING / APPROVED / REJECTED
    private Instant createdAt;

    public TransactionEventDto() {
    }

    public TransactionEventDto(UUID transactionId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }
    public TransactionEventDto(UUID transactionId, BigDecimal amount, String type) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}