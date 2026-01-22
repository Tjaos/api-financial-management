package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateways.PasswordHasher;
import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.application.services.UserSpreadsheetParser;
import br.com.finance.ms_user.user.domain.entities.user.User;

import java.io.InputStream;
import java.util.List;

public class BulkCreateUsers {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserSpreadsheetParser parser;

    public BulkCreateUsers(UserRepository userRepository,
                           PasswordHasher passwordHasher,
                           UserSpreadsheetParser parser) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.parser = parser;
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
        }

        return users.size();
    }
}
