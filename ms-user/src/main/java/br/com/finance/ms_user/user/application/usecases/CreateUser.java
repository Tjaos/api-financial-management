package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.exceptions.EmailAlreadyExistsException;
import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import br.com.finance.ms_user.user.domain.exceptions.InvalidPasswordSizeException;

public class CreateUser {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUser(UserRepository userRepository,PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }


    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        if(user.getPassword().length() < 6) {
            throw new InvalidPasswordSizeException();
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

