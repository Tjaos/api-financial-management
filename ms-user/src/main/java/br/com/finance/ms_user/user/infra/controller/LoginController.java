package br.com.finance.ms_user.user.infra.controller;

import br.com.finance.ms_user.user.application.usecases.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginUser loginUser;

    public LoginController(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request) {
        String token = loginUser.login(
                request.email(),
                request.password()
        );
        LoginResponseDto response = new LoginResponseDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
