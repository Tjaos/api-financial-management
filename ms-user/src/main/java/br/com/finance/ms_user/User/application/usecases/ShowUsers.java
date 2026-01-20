package br.com.finance.ms_user.User.application.usecases;

import br.com.finance.ms_user.User.application.gateways.UserRepository;
import br.com.finance.ms_user.User.domain.entities.user.User;

import java.util.List;

public class ShowUsers {

    private final UserRepository userRepository;

    public ShowUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUserByEmail(String email) {
        return this.userRepository.findUser(email);
    }
}
