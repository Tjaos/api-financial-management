package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;

import java.util.UUID;

public class UpdateUser {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;


    public UpdateUser(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User updateUser(UUID id, String name, String email, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(name != null && !name.isBlank()) {
            user.changeName(name);
        }
        if(email != null && !email.isBlank()){
            user.changeEmail(email);
        }
        if(password != null && !password.isBlank()){
            user.changePassword(passwordHasher.hash(password));
        }
        return userRepository.save(user);
    }
}
