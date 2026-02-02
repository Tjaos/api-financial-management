package br.com.finance.ms_user.user.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserResponseDto(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", description = "ID do usuário")
        UUID id,
        @Schema(example = "João Silva", description = "Nome do usuário")
        String name,
        @Schema(example = "joao@email.com", description = "Email do usuário")
        String email

) {
}
