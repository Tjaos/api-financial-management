package br.com.finance.ms_user.user.infra.controller;

import java.util.UUID;

public record UserDto(
        UUID id,
        String name,
        String email,
        String password
) {
}
