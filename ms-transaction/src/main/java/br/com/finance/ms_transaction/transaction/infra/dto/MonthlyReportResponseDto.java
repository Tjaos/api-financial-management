package br.com.finance.ms_transaction.transaction.infra.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MonthlyReportResponseDto(
        int month,
        int year,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        List<Item> transactions
) {
    public record Item(
            UUID id,
            String type,
            BigDecimal amount,
            String category,
            String description,
            String status,
            Instant createdAt
    ){}
}
