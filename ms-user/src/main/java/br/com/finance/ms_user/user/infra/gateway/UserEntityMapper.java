package br.com.finance.ms_user.user.infra.gateway;

import br.com.finance.ms_user.user.domain.entities.user.User;
import br.com.finance.ms_user.user.infra.persistence.UserEntity;

public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword() //senha deve estar com hash
        );
    }

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword()
        );
    }
}
