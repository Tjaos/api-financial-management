package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.exceptions.UserOrPasswordWrongException;
import br.com.finance.ms_user.user.application.gateways.PasswordHasher;
import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.application.security.TokenService;
import br.com.finance.ms_user.user.domain.entities.user.User;


public class LoginUser {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public LoginUser(UserRepository userRepository, PasswordHasher passwordHasher, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenService = tokenService;
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserOrPasswordWrongException::new);

        if (!passwordHasher.matches(rawPassword, user.getPassword())) {
            throw new UserOrPasswordWrongException();
        }


        return tokenService.generateToken(user);
    }


}
