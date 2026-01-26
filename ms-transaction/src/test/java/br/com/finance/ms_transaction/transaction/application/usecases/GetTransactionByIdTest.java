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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTransactionByIdTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private GetTransactionById getTransactionById;

    @Test
    void shouldReturnTransactionWhenExistsAndBelongsToUser() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Transaction transaction = new Transaction(
                transactionId,
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(100),
                "BRL",
                "Salary",
                "Monthly salary",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        Transaction result = getTransactionById.getTransaction(transactionId, userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(transactionId);
        assertThat(result.getUserId()).isEqualTo(userId);

        verify(repository, times(1)).findById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(repository.findById(transactionId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getTransactionById.getTransaction(transactionId, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transação não encontrada");

        verify(repository, times(1)).findById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotBelongToUser() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Transaction transaction = new Transaction(
                transactionId,
                otherUserId, // outro usuário
                TransactionType.WITHDRAW,
                BigDecimal.valueOf(50),
                "BRL",
                "Food",
                "Lunch",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> getTransactionById.getTransaction(transactionId, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Acesso negado!");

        verify(repository, times(1)).findById(transactionId);
    }
}