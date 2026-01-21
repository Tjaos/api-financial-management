package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateways.PasswordHasher;
import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;

public class CreateUser {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUser(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }


    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        String hashedPassword = passwordHasher.hash(user.getPassword());

        User userWithHashedPassword = new User(
                user.getName(),
                user.getEmail(),
                hashedPassword
        );
        return userRepository.save(userWithHashedPassword);
    }
}

