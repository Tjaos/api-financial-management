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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTransactionTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DeleteTransaction deleteTransaction;

    @Test
    void shouldDeleteTransactionWhenPendingAndBelongsToUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        Transaction transaction = new Transaction(
                transactionId,
                userId,
                TransactionType.PURCHASE,
                new BigDecimal("100.00"),
                "BRL",
                "FOOD",
                "Almoço",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        // Act
        deleteTransaction.delete(userId, transactionId);

        // Assert
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> deleteTransaction.delete(userId, transactionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transação não encontrada");

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotBelongToUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();

        Transaction transaction = new Transaction(
                transactionId,
                anotherUserId, // outro dono
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                "USD",
                "SALARY",
                "Salário",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        // Act + Assert
        assertThatThrownBy(() -> deleteTransaction.delete(userId, transactionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Acesso negado!");

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenTransactionIsNotPending() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        Transaction transaction = new Transaction(
                transactionId,
                userId,
                TransactionType.PURCHASE,
                new BigDecimal("250.00"),
                "BRL",
                "SHOPPING",
                "Tênis novo",
                TransactionStatus.APPROVED, // não pendente
                Instant.now()
        );

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        // Act + Assert
        assertThatThrownBy(() -> deleteTransaction.delete(userId, transactionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Apenas transações pendentes podem ser deletadas");

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).deleteById(any());
    }

}