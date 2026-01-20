package br.com.finance.ms_user.User.application.usecases;

import br.com.finance.ms_user.User.application.gateways.UserRepository;
import br.com.finance.ms_user.User.domain.entities.user.User;

public class CreateUser {

    private final UserRepository userRepository;

    public CreateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.createUser(user);
    }
}
