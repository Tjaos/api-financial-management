package br.com.finance.ms_user.user.application.gateway;

import br.com.finance.ms_user.user.domain.entities.user.User;

public interface TokenService {
    String generateToken(User user);
    boolean isTokenValid(String token);
    String getSubject(String token);
}
