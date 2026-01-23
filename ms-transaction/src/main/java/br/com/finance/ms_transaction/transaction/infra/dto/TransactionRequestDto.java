package br.com.finance.ms_transaction.transaction.infra.dto;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionRequestDto(
        TransactionType type,
        BigDecimal amount,
        String currency,
        String category,
        String description
) {
}
