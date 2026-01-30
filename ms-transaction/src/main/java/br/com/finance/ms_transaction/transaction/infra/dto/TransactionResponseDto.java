package br.com.finance.ms_transaction.transaction.infra.dto;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponseDto(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", description = "ID da transação")
        UUID id,
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", description = "ID do usuário")
        UUID userId,
        @Schema(example = "DEPOSIT", description = "Tipo da transação")
        TransactionType type,
        @Schema(example = "1500.00", description = "Valor da transação")
        BigDecimal amount,
        @Schema(example = "BRL", description = "Moeda da transação")
        String currency,
        @Schema(example = "Salário", description = "Categoria da transação")
        String category,
        TransactionStatus status,
        @Schema(example = "Depósito do salário mensal", description = "Descrição da transação")
        String description,
        Instant createdAt
) {
}
