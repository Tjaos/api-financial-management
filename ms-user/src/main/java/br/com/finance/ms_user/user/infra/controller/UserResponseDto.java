package br.com.finance.ms_user.user.infra.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserResponseDto(
        @NotNull
        UUID id,
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email

) {
}
