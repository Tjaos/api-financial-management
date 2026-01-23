package br.com.finance.ms_transaction.transaction.infra.dto;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        UUID userId,
        TransactionType type,
        BigDecimal amount,
        String currency,
        String category,
        TransactionStatus status,
        Instant createdAt
) {
}
