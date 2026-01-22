package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.exceptions.EmailAlreadyExistsException;
import br.com.finance.ms_user.user.application.gateways.PasswordHasher;
import br.com.finance.ms_user.user.application.gateways.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private CreateUser createUser;

    @Test
    void shouldCreateUserWithHashedPassword() {
        // Arrange
        User inputUser = new User("Thiago", "thiago@email.com", "123456");
        String hashedPassword = "hashed-123";

        when(userRepository.existsByEmail("thiago@email.com"))
                .thenReturn(false);

        when(passwordHasher.hash("123456"))
                .thenReturn(hashedPassword);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = createUser.createUser(inputUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Thiago");
        assertThat(result.getEmail()).isEqualTo("thiago@email.com");
        assertThat(result.getPassword()).isEqualTo(hashedPassword);

        verify(userRepository, times(1)).existsByEmail("thiago@email.com");
        verify(passwordHasher, times(1)).hash("123456");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        User inputUser = new User("Thiago", "thiago@email.com", "123456");

        when(userRepository.existsByEmail("thiago@email.com"))
                .thenReturn(true);

        // Act + Assert
        RuntimeException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> createUser.createUser(inputUser)
        );

        assertThat(exception.getMessage()).isEqualTo("Email já cadastrado");

        verify(userRepository, times(1)).existsByEmail("thiago@email.com");
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldHashPasswordBeforeSavingUser() {
        // Arrange
        User inputUser = new User("Thiago", "thiago@email.com", "plain");

        when(userRepository.existsByEmail(any()))
                .thenReturn(false);

        when(passwordHasher.hash("plain"))
                .thenReturn("hashed-plain");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        createUser.createUser(inputUser);

        // Assert
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo("Thiago");
        assertThat(savedUser.getEmail()).isEqualTo("thiago@email.com");
        assertThat(savedUser.getPassword()).isEqualTo("hashed-plain");
        assertThat(savedUser.getPassword()).isNotEqualTo("plain");
    }

}