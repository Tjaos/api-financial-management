package br.com.finance.ms_transaction.transaction.application.dto;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MonthlyTransactionReport(
        int month,
        int year,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        List<Item> items
) {

    public record Item(
            UUID id,
            TransactionType type,
            BigDecimal amount,
            String category,
            String description,
            TransactionStatus status,
            Instant createdAt
    ) {}
}
