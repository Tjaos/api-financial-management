package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import br.com.finance.ms_user.user.kafka.event.UserCreatedEvent;
import br.com.finance.ms_user.user.kafka.producer.UserCreatedProducer;

import java.io.InputStream;
import java.util.List;

public class BulkCreateUsers {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserSpreadsheetParser parser;
    private final UserCreatedProducer userCreatedProducer;

    public BulkCreateUsers(UserRepository userRepository,
                           PasswordHasher passwordHasher,
                           UserSpreadsheetParser parser, UserCreatedProducer userCreatedProducer) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.parser = parser;
        this.userCreatedProducer = userCreatedProducer;
    }

    public int upload(InputStream inputStream) {
        List<User> users = parser.parse(inputStream);

        for (User user : users) {

            if (userRepository.existsByEmail(user.getEmail())) {
                continue;
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
        }

        return users.size();
    }
}
