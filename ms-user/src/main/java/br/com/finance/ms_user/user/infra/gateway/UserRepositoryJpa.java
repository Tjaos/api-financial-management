package br.com.finance.ms_user.user.infra.gateway;

import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import br.com.finance.ms_user.user.infra.persistence.UserEntity;
import br.com.finance.ms_user.user.infra.persistence.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryJpa implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;


    public UserRepositoryJpa(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User registerUser(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }


    @Override
    public List<User> getAllUser() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public User updateUser(String email, User user) {
        UserEntity existing = jpaRepository.findByEmail(email);
        if(existing != null){
            UserEntity updated = mapper.toEntity(user);
            updated.setId(existing.getId());
            jpaRepository.save(updated);
            return mapper.toDomain(updated);
        }
            return null;

    }

    @Override
    public void deleteUser(String email) {
        UserEntity entity = jpaRepository.findByEmail(email);
        jpaRepository.deleteById(entity.getId());

    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
