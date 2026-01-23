package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.application.gateway.UserRepository;
import br.com.finance.ms_user.user.domain.entities.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowUsersTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShowUsers showUsers;

    @Test
    void shouldReturnAllUsersWithPagination() {
        // Arrange
        int page = 0;
        int size = 10;

        User user1 = new User("Thiago", "thiago@email.com", "hashed-123");
        User user2 = new User("Maria", "maria@email.com", "hashed-456");

        List<User> users = List.of(user1, user2);

        when(userRepository.getUsers(page, size))
                .thenReturn(users);

        // Act
        List<User> result = showUsers.findUsers(page, size);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(user1, user2);

        verify(userRepository, times(1))
                .getUsers(page, size);
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        // Arrange
        int page = 0;
        int size = 10;

        when(userRepository.getUsers(page, size))
                .thenReturn(List.of());

        // Act
        List<User> result = showUsers.findUsers(page, size);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(userRepository, times(1))
                .getUsers(page, size);
    }

}