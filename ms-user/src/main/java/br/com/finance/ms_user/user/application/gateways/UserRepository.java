package br.com.finance.ms_user.user.application.gateways;

import br.com.finance.ms_user.user.domain.entities.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<User> getAllUser();

    User save(User user);

    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean existsByEmail(String email);
}
