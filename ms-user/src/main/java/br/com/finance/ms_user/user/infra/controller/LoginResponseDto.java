package br.com.finance.ms_user.user.infra.controller;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDto(
        @NotBlank
        String token
) {
}
