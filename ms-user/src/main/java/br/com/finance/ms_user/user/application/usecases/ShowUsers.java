package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;

import java.util.List;

public class ShowUsers {

    private final UserRepository userRepository;

    public ShowUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUsers(int page, int size) {
        if(page < 0 ){
            page = 0;
        }
        if(size > 100 ){
            size = 100;
        }
        return this.userRepository.getUsers(page, size);
    }
}
