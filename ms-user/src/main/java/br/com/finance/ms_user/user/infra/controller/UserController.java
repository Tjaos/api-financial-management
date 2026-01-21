package br.com.finance.ms_user.user.infra.controller;

import br.com.finance.ms_user.user.application.usecases.CreateUser;
import br.com.finance.ms_user.user.application.usecases.DeleteUser;
import br.com.finance.ms_user.user.application.usecases.ShowUsers;
import br.com.finance.ms_user.user.application.usecases.UpdateUser;
import br.com.finance.ms_user.user.domain.entities.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userDto) {
        User newUser = createUser.createUser(
                new User(
                        userDto.name(),
                        userDto.email(),
                        userDto.password()
                )
        );
        UserResponseDto response = new UserResponseDto(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return showUsers.findAllUsers().stream()
                .map(u-> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .toList();
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable String email,
            @RequestBody UserRequestDto userDto){

        User updated = updateUser.updateUser(
                email,
                new User(userDto.name(), userDto.email(), userDto.password())
        );

        UserResponseDto response = new UserResponseDto(
                updated.getId(),
                updated.getName(),
                updated.getEmail()
        );
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String email) {
        deleteUser.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
