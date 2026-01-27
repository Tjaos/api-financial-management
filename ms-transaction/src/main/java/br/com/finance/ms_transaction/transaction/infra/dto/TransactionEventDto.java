package br.com.finance.ms_transaction.transaction.infra.dto;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionEventDto(
        UUID transactionId,
        UUID userId,
        BigDecimal amount,
        String currency,
        TransactionType type
) {
}
