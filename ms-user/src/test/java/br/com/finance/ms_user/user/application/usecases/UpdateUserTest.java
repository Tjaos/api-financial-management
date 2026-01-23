package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateway.PasswordHasher;
import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UpdateUser updateUser;

    @Test
    void shouldUpdateAllFieldsSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();

        String oldName = "Thiago";
        String oldEmail = "thiago@email.com";
        String oldHashedPassword = "hashed-old";

        String newName = "Thiago Amaral";
        String newEmail = "thiago.novo@email.com";
        String newRawPassword = "nova-senha";
        String newHashedPassword = "hashed-new";

        User existingUser = new User(id, oldName, oldEmail, oldHashedPassword);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        when(passwordHasher.hash(newRawPassword))
                .thenReturn(newHashedPassword);

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        User result = updateUser.updateUser(id, newName, newEmail, newRawPassword);

        // Assert
        assertThat(result.getName()).isEqualTo(newName);
        assertThat(result.getEmail()).isEqualTo(newEmail);
        assertThat(result.getPassword()).isEqualTo(newHashedPassword);

        verify(userRepository, times(1)).findById(id);
        verify(passwordHasher, times(1)).hash(newRawPassword);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                RuntimeException.class,
                () -> updateUser.updateUser(id, "Novo Nome", "novo@email.com", "nova-senha")
        );

        verify(userRepository, times(1)).findById(id);
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateOnlyNameWhenOtherFieldsAreNull() {
        // Arrange
        UUID id = UUID.randomUUID();

        String oldName = "Thiago";
        String oldEmail = "thiago@email.com";
        String oldHashedPassword = "hashed-old";

        String newName = "Thiago Atualizado";

        User existingUser = new User(id, oldName, oldEmail, oldHashedPassword);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        User result = updateUser.updateUser(id, newName, null, null);

        // Assert
        assertThat(result.getName()).isEqualTo(newName);
        assertThat(result.getEmail()).isEqualTo(oldEmail);
        assertThat(result.getPassword()).isEqualTo(oldHashedPassword);

        verify(userRepository, times(1)).findById(id);
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void shouldUpdateOnlyPasswordWhenProvided() {
        // Arrange
        UUID id = UUID.randomUUID();

        String oldName = "Thiago";
        String oldEmail = "thiago@email.com";
        String oldHashedPassword = "hashed-old";

        String newRawPassword = "nova-senha";
        String newHashedPassword = "hashed-new";

        User existingUser = new User(id, oldName, oldEmail, oldHashedPassword);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        when(passwordHasher.hash(newRawPassword))
                .thenReturn(newHashedPassword);

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        // Act
        User result = updateUser.updateUser(id, null, null, newRawPassword);

        // Assert
        assertThat(result.getPassword()).isEqualTo(newHashedPassword);
        assertThat(result.getName()).isEqualTo(oldName);
        assertThat(result.getEmail()).isEqualTo(oldEmail);

        verify(userRepository, times(1)).findById(id);
        verify(passwordHasher, times(1)).hash(newRawPassword);
        verify(userRepository, times(1)).save(existingUser);
    }

}