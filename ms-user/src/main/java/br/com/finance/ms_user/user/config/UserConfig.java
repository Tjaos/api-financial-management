package br.com.finance.ms_user.user.config;

import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.application.usecases.CreateUser;
import br.com.finance.ms_user.user.application.usecases.DeleteUser;
import br.com.finance.ms_user.user.application.usecases.ShowUsers;
import br.com.finance.ms_user.user.application.usecases.UpdateUser;
import br.com.finance.ms_user.user.infra.gateway.UserEntityMapper;
import br.com.finance.ms_user.user.infra.gateway.UserRepositoryJpa;
import br.com.finance.ms_user.user.infra.persistence.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CreateUser createUser(UserRepository userRepository) {
        return new CreateUser(userRepository);
    }

    @Bean
    ShowUsers showUsers(UserRepository userRepository) {
        return new ShowUsers(userRepository);
    }

    @Bean
    UpdateUser updateUser(UserRepository userRepository) {
        return new UpdateUser(userRepository);
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
    UserRepositoryJpa userRepositoryJpa(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        return new UserRepositoryJpa(jpaRepository, mapper);
    }
}
