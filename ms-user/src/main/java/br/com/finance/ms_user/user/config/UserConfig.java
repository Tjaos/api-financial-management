package br.com.finance.ms_user.user.config;

import br.com.finance.ms_user.user.application.gateways.PasswordHasher;
import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.application.security.TokenService;
import br.com.finance.ms_user.user.application.usecases.*;
import br.com.finance.ms_user.user.infra.security.BCryptPasswordHasher;
import br.com.finance.ms_user.user.infra.gateway.UserEntityMapper;
import br.com.finance.ms_user.user.infra.gateway.UserRepositoryJpa;
import br.com.finance.ms_user.user.infra.persistence.UserJpaRepository;
import br.com.finance.ms_user.user.infra.security.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    CreateUser createUser(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new CreateUser(userRepository, passwordHasher);
    }

    @Bean
    ShowUsers showUsers(UserRepository userRepository) {
        return new ShowUsers(userRepository);
    }

    @Bean
    UpdateUser updateUser(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new UpdateUser(userRepository, passwordHasher);
    }

    @Bean
    DeleteUser deleteUser(UserRepository userRepository) {
        return new DeleteUser(userRepository);
    }

    @Bean
    UserEntityMapper returnMapper() {
        return new UserEntityMapper();
    }

    @Bean
    UserRepository userRepository(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        return new UserRepositoryJpa(jpaRepository, mapper);
    }

    @Bean
    LoginUser loginUser(UserRepository userRepository, PasswordHasher passwordHasher, TokenService tokenService) {
        return new LoginUser(userRepository, passwordHasher, tokenService );
    }

    @Bean
    JwtTokenService jwtTokenService() {
        return new JwtTokenService(
                "chave-secreta-exemplo-para-geracao-de-tokens-1234567890",
                1000L * 60 * 60
        );
    }


    @Bean
    PasswordHasher passwordHasher(PasswordEncoder encoder) {
        return new BCryptPasswordHasher(encoder);
    }


}
