package br.com.finance.ms_user.user.infra.controller;

import br.com.finance.ms_user.user.application.usecases.LoginUser;
import br.com.finance.ms_user.user.infra.dto.LoginRequestDto;
import br.com.finance.ms_user.user.infra.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Obter token JWT",
            description = "Gera um token JWT para autenticação do usuário"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token gerado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
