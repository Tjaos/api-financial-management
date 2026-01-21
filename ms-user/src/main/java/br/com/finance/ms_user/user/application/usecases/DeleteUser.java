package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateways.UserRepository;

import java.util.UUID;

public class DeleteUser {
    private final UserRepository userRepository;


    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }
}
