package br.com.finance.ms_user.user.infra.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email

) {
}
