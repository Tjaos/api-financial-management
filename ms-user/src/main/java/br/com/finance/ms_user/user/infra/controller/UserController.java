package br.com.finance.ms_user.user.infra.controller;

import br.com.finance.ms_user.user.application.usecases.CreateUser;
import br.com.finance.ms_user.user.application.usecases.DeleteUser;
import br.com.finance.ms_user.user.application.usecases.ShowUsers;
import br.com.finance.ms_user.user.application.usecases.UpdateUser;
import br.com.finance.ms_user.user.domain.entities.user.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUser createUser;
    private final DeleteUser deleteUser;
    private final UpdateUser updateUser;
    private final ShowUsers showUsers;


    public UserController(CreateUser createUser, DeleteUser deleteUser, UpdateUser updateUser, ShowUsers showUsers) {
        this.createUser = createUser;
        this.deleteUser = deleteUser;
        this.updateUser = updateUser;
        this.showUsers = showUsers;
    }

    @PostMapping("/register")
    public UserDto createUser(@RequestBody UserDto userDto) {
        User newUser = createUser.createUser(
                new User(
                        userDto.name(),
                        userDto.email(),
                        userDto.password()
                )
        );
        return new UserDto(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail(),
                newUser.getPassword()
        );
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return showUsers.findAllUsers().stream()
                .map(u-> new UserDto(u.getId(), u.getName(), u.getEmail(), u.getPassword()))
                .collect(Collectors.toList());
    }

    @PutMapping("/{email}")
    public UserDto updateUser(@PathVariable String email, @RequestBody UserDto userDto){
        User updated = updateUser.updateUser(email, new User(userDto.id(), userDto.name(), userDto.email(), userDto.password()));
        return new UserDto(updated.getId(), updated.getName(), updated.getEmail(), updated.getPassword());
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        deleteUser.deleteUserByEmail(email);
    }
}
