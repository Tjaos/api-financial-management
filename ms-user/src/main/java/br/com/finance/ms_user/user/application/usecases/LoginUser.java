package br.com.finance.ms_user.user.application.usecases;

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
        try{
            User user = userRepository.findByEmail(email);
            if(!passwordHasher.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Usuário ou senha incorretos");
            }
            return tokenService.generateToken(user);
        }catch(Exception e){
            throw new RuntimeException("Usuário ou senha incorretos");
        }

    }
}
