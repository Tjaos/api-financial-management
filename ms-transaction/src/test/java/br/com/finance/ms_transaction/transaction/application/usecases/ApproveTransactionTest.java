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
class ApproveTransactionTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private ApproveTransaction approveTransaction;

    @Test
    void shouldApprovePendingTransaction() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Transaction pending = new Transaction(
                transactionId,
                userId,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(100),
                "BRL",
                "Salário",
                "Pagamento mensal",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId)).thenReturn(Optional.of(pending));
        when(repository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = approveTransaction.approve(transactionId);

        assertThat(result.getStatus()).isEqualTo(TransactionStatus.APPROVED);
        verify(repository).findById(transactionId);
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        UUID transactionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(repository.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> approveTransaction.approve(transactionId))
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
                TransactionType.PURCHASE,
                BigDecimal.valueOf(50),
                "BRL",
                "Lanche",
                "Café",
                TransactionStatus.PENDING,
                Instant.now()
        );

        when(repository.findById(transactionId)).thenReturn(Optional.of(pending));

        assertThatThrownBy(() -> approveTransaction.approve(transactionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Acesso negado! Usuário não autorizado a aprovar esta transação.");

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
                BigDecimal.valueOf(200),
                "BRL",
                "Bônus",
                "Extra",
                TransactionStatus.APPROVED,
                Instant.now()
        );

        when(repository.findById(transactionId)).thenReturn(Optional.of(approved));

        assertThatThrownBy(() -> approveTransaction.approve(transactionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("A transação não está pendente e não pode ser aprovada.");

        verify(repository).findById(transactionId);
        verify(repository, never()).save(any());
    }
}