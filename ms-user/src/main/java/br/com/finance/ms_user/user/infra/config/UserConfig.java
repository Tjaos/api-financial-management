package br.com.finance.ms_user.user.infra.config;

import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.application.gateway.TokenService;
import br.com.finance.ms_user.user.application.usecases.UserSpreadsheetParser;
import br.com.finance.ms_user.user.application.usecases.*;
import br.com.finance.ms_user.user.infra.security.BCryptPasswordHasher;
import br.com.finance.ms_user.user.infra.gateway.UserEntityMapper;
import br.com.finance.ms_user.user.infra.gateway.UserRepositoryImpl;
import br.com.finance.ms_user.user.infra.persistence.UserJpaRepository;
import br.com.finance.ms_user.user.infra.security.JwtTokenService;
import br.com.finance.ms_user.user.kafka.producer.UserCreatedProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    @Bean
    CreateUser createUser(UserRepository userRepository, PasswordHasher passwordHasher, UserCreatedProducer userCreatedProducer) {
        return new CreateUser(userRepository, passwordHasher,  userCreatedProducer);
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
        return new UserRepositoryImpl(jpaRepository, mapper);
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



    @Bean
    BulkCreateUsers bulkCreateUsers(UserRepository userRepository, PasswordHasher passwordHasher, UserSpreadsheetParser spreadsheetParser, UserCreatedProducer userCreatedProducer) {
        return new BulkCreateUsers(userRepository, passwordHasher, spreadsheetParser,  userCreatedProducer);
    }

    @Bean
    UserSpreadsheetParser userSpreadsheetParser() {
        return new UserSpreadsheetParser();
    }

}
