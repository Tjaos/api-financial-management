package br.com.finance.ms_user.User.application.usecases;

import br.com.finance.ms_user.User.application.gateways.UserRepository;

public class DeleteUser {
    private final UserRepository userRepository;


    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteUser(email);
    }
}
