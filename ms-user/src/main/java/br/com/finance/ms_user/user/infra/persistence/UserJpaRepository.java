package br.com.finance.ms_user.user.infra.persistence;

import br.com.finance.ms_user.user.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);



}
