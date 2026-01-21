package br.com.finance.ms_user.user.application.gateways;

import br.com.finance.ms_user.user.domain.entities.user.User;

import java.util.List;

public interface UserRepository {

    User registerUser(User user);

    List<User> getAllUser();

    User updateUser(String email, User user);

    void deleteUser(String email);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
