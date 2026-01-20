package br.com.finance.ms_user.User.application.gateways;

import br.com.finance.ms_user.User.domain.entities.user.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);
    List<User> findUser(String email);
    User updateUser(String email, User user);
    void deleteUser(String email);
}
