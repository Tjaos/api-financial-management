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
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTransactionTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private UpdateTransaction updateTransaction;

    @Test
    void shouldUpdateTransactionSuccessfully() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        Transaction existing = new Transaction(
                transactionId,
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(100),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.PENDING,
                createdAt
        );

        when(repository.findById(transactionId))
                .thenReturn(Optional.of(existing));

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = updateTransaction.update(
                transactionId,
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(200),
                "USD",
                "Salary",
                "Monthly salary"
        );

        // Assert
        assertThat(result.getId()).isEqualTo(transactionId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        assertThat(result.getCurrency()).isEqualTo("USD");
        assertThat(result.getCategory()).isEqualTo("Salary");
        assertThat(result.getDescription()).isEqualTo("Monthly salary");
        assertThat(result.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);

        verify(repository, times(1)).findById(transactionId);
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldKeepOldValuesWhenParamsAreNull() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        Transaction existing = new Transaction(
                transactionId,
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(100),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.PENDING,
                createdAt
        );

        when(repository.findById(transactionId))
                .thenReturn(Optional.of(existing));

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = updateTransaction.update(
                transactionId,
                userId,
                null,
                null,
                null,
                null,
                null
        );

        // Assert
        assertThat(result.getType()).isEqualTo(existing.getType());
        assertThat(result.getAmount()).isEqualByComparingTo(existing.getAmount());
        assertThat(result.getCurrency()).isEqualTo(existing.getCurrency());
        assertThat(result.getCategory()).isEqualTo(existing.getCategory());
        assertThat(result.getDescription()).isEqualTo(existing.getDescription());
        assertThat(result.getStatus()).isEqualTo(existing.getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(existing.getCreatedAt());

        verify(repository, times(1)).findById(transactionId);
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(repository.findById(transactionId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> updateTransaction.update(
                transactionId,
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(200),
                "USD",
                "Salary",
                "Monthly salary"
        )).isInstanceOf(RuntimeException.class)
                .hasMessage("Transação não encontrada");

        verify(repository, times(1)).findById(transactionId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();

        Transaction existing = new Transaction(
                transactionId,
                ownerId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(100),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId))
                .thenReturn(Optional.of(existing));

        // Act + Assert
        assertThatThrownBy(() -> updateTransaction.update(
                transactionId,
                anotherUserId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(200),
                "USD",
                "Salary",
                "Monthly salary"
        )).isInstanceOf(RuntimeException.class)
                .hasMessage("Acesso negado!");

        verify(repository, times(1)).findById(transactionId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTransactionIsNotPending() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Transaction existing = new Transaction(
                transactionId,
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(100),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.APPROVED,
                Instant.now()
        );

        when(repository.findById(transactionId))
                .thenReturn(Optional.of(existing));

        // Act + Assert
        assertThatThrownBy(() -> updateTransaction.update(
                transactionId,
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(200),
                "USD",
                "Salary",
                "Monthly salary"
        )).isInstanceOf(RuntimeException.class)
                .hasMessage("Apenas transções pendentes podem ser atualizadas!");

        verify(repository, times(1)).findById(transactionId);
        verify(repository, never()).save(any());
    }
}