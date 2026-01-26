package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListTransactionByUserTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private ListTransactionByUser listTransactionByUser;

    @Test
    void shouldReturnListOfTransactionsForUser() {
        // Arrange
        UUID userId = UUID.randomUUID();

        Transaction t1 = new Transaction(
                UUID.randomUUID(),
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(100),
                "BRL",
                "Salary",
                "Monthly salary",
                TransactionStatus.PENDING,
                Instant.now()
        );

        Transaction t2 = new Transaction(
                UUID.randomUUID(),
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(50),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findByUserId(userId))
                .thenReturn(List.of(t1, t2));

        // Act
        List<Transaction> result = listTransactionByUser.list(userId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(t1, t2);

        verify(repository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoTransactions() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(repository.findByUserId(userId))
                .thenReturn(List.of());

        // Act
        List<Transaction> result = listTransactionByUser.list(userId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(repository, times(1)).findByUserId(userId);
    }
}