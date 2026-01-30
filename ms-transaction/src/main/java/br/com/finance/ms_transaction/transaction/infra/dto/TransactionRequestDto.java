package br.com.finance.ms_transaction.transaction.infra.dto;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDto(

        @Schema(example = "DEPOSIT", description = "Tipo da transação")
        @NotNull
        TransactionType type,
        @Schema(example = "1500.00", description = "Valor da transação")
        @NotNull
        BigDecimal amount,
        @Schema(example = "BRL", description = "Moeda da transação")
        @NotNull
        String currency,
        @Schema(example = "Depósito do salário mensal", description = "Categoria da transação")
        String category,
        TransactionStatus status,
        @Schema(example = "Depósito do salário mensal", description = "Descrição da transação")
        String description
) {
}
