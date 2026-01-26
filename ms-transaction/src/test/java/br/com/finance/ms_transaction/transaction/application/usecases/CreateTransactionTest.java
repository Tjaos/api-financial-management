package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CreateTransaction createTransaction;

    @Test
    void shouldCreateTransactionWithPendingStatusAndCurrentTimestamp() {
        // Arrange
        UUID userId = UUID.randomUUID();
        TransactionType type = TransactionType.PURCHASE;
        BigDecimal amount = new BigDecimal("150.75");
        String currency = "BRL";
        String category = "FOOD";
        String description = "Almoço no restaurante";

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Transaction> transactionCaptor =
                ArgumentCaptor.forClass(Transaction.class);

        // Act
        Transaction result = createTransaction.create(
                userId,
                type,
                amount,
                currency,
                category,
                description
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getType()).isEqualTo(type);
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getCurrency()).isEqualTo(currency);
        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedAt()).isBeforeOrEqualTo(Instant.now());

        verify(transactionRepository, times(1)).save(transactionCaptor.capture());

        Transaction saved = transactionCaptor.getValue();

        assertThat(saved.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldCallRepositorySaveOnce() {
        // Arrange
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        createTransaction.create(
                UUID.randomUUID(),
                TransactionType.DEPOSIT,
                new BigDecimal("300.00"),
                "USD",
                "SALARY",
                "Salário mensal"
        );

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


}