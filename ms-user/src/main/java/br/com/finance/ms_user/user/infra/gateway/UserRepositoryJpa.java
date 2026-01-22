package br.com.finance.ms_user.user.infra.gateway;

import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import br.com.finance.ms_user.user.infra.persistence.UserEntity;
import br.com.finance.ms_user.user.infra.persistence.UserJpaRepository;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class UserRepositoryJpa implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;


    public UserRepositoryJpa(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public List<User> getUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return jpaRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(jpaRepository.findByEmail(email))
                .map((mapper::toDomain));
    }


    @Override
    public void deleteById(UUID id) {
        if(!jpaRepository.existsById(id)){
            throw new RuntimeException("Usuário não encontrado");
        }
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }


}
