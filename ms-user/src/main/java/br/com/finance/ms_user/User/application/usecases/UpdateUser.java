package br.com.finance.ms_user.User.application.usecases;

import br.com.finance.ms_user.User.application.gateways.UserRepository;
import br.com.finance.ms_user.User.domain.entities.user.User;

public class UpdateUser {
    private final UserRepository userRepository;


    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(String email, User user) {
        return userRepository.updateUser(email, user);
    }
}
