package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.exceptions.EmailAlreadyExistsException;
import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import br.com.finance.ms_user.user.domain.exceptions.InvalidPasswordSizeException;
import br.com.finance.ms_user.user.kafka.event.UserCreatedEvent;
import br.com.finance.ms_user.user.kafka.producer.UserCreatedProducer;

public class CreateUser {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserCreatedProducer userCreatedProducer;

    public CreateUser(UserRepository userRepository, PasswordHasher passwordHasher, UserCreatedProducer userCreatedProducer) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.userCreatedProducer = userCreatedProducer;
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
        userRepository.save(userWithHashedPassword);
        userCreatedProducer.send(
            new UserCreatedEvent(user.getId(), user.getEmail())
        );

        return userWithHashedPassword;
    }
}

