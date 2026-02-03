package br.com.finance.ms_transaction.transaction.infra.dto;

import java.math.BigDecimal;
import java.util.List;

public record TransactionReportDto(
        int month,
        int year,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        List<TransactionResponseDto> transactions
) {

}
