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
class RejectTransactionTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private RejectTransaction rejectTransaction;

    @Test
    void shouldRejectPendingTransaction() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Transaction pending = new Transaction(
                transactionId,
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(70),
                "BRL",
                "Mercado",
                "Compra",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId)).thenReturn(Optional.of(pending));
        when(repository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = rejectTransaction.reject(transactionId, userId);

        assertThat(result.getStatus()).isEqualTo(TransactionStatus.REJECTED);
        verify(repository).findById(transactionId);
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(repository.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rejectTransaction.reject(transactionId, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transação não encontrada");

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        UUID transactionId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Transaction pending = new Transaction(
                transactionId,
                ownerId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(120),
                "BRL",
                "Freela",
                "Projeto",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId)).thenReturn(Optional.of(pending));

        assertThatThrownBy(() -> rejectTransaction.reject(transactionId, otherUserId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Acesso negado! Usuário não autorizado a rejeitar esta transação.");

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTransactionIsNotPending() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Transaction approved = new Transaction(
                transactionId,
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(300),
                "BRL",
                "Venda",
                "Produto",
                TransactionStatus.APPROVED,
                Instant.now()
        );

        when(repository.findById(transactionId)).thenReturn(Optional.of(approved));

        assertThatThrownBy(() -> rejectTransaction.reject(transactionId, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("A transação não está pendente e não pode ser rejeitada.");

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any());
    }
}