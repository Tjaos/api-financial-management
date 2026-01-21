package br.com.finance.ms_user.user.application.security;

import br.com.finance.ms_user.user.domain.entities.user.User;

import java.util.Optional;

public interface TokenService {
    String generateToken(User user);
}
