package br.com.finance.ms_user.user.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(example = "João Silva", description = "Nome do usuário")
        String name,
        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        @Schema(example = "joao@email.com", description = "Email do usuário")
        String email,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password
) {
}
