package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;

import java.util.List;

public class ShowUsers {

    private final UserRepository userRepository;

    public ShowUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return this.userRepository.getAllUser();
    }
}
