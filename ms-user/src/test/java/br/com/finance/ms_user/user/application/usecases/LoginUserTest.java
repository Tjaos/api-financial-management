package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.exceptions.UserOrPasswordWrongException;
import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.application.gateway.TokenService;
import br.com.finance.ms_user.user.domain.entities.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginUser loginUser;

    @Test
    void shouldLoginUserSuccessfullyAndReturnToken() {
        //Arrange
        String name = "Thiago";
        String email = "thiago@email.com";
        String password = "123456";
        String hashedPassword = "hashed-123";
        String token = "jwt-token";

        User user = new User(name,email, hashedPassword);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(password, hashedPassword))
                .thenReturn(true);

        when(tokenService.generateToken(user))
                .thenReturn(token);

        //Act
        String result = loginUser.login(email, password);

        //Assert
        assertThat(result).isEqualTo(token);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordHasher, times(1)).matches(password, hashedPassword);
        verify(tokenService, times(1)).generateToken(user);


    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExists() {
        //Arrange
        String email = "emailquenaoexiste@email.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        //Act + Assert
        assertThrows(
                UserOrPasswordWrongException.class,
                () -> loginUser.login(email, "senha-qualquer")
        );

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordHasher, never()).matches(any(), any());
        verify(tokenService, never()).generateToken(any());


    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWrong() {
        // Arrange
        String email = "thiago@email.com";
        String rawPassword = "senha-errada";
        String hashedPassword = "hashed-123";

        User user = new User("Thiago", email, hashedPassword);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(rawPassword, hashedPassword))
                .thenReturn(false);

        // Act + Assert
        assertThrows(
                UserOrPasswordWrongException.class,
                () -> loginUser.login(email, rawPassword)
        );

        verify(userRepository, times(1))
                .findByEmail(email);

        verify(passwordHasher, times(1))
                .matches(rawPassword, hashedPassword);

        verify(tokenService, never())
                .generateToken(any());
    }

}