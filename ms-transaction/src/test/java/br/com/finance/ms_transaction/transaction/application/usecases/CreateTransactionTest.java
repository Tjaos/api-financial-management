package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.ExchangeRateGateway;
import br.com.finance.ms_transaction.transaction.application.gateways.TransactionEventPublisher;
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
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionEventPublisher publisher;

    @Mock
    private ExchangeRateGateway exchangeRateGateway;

    @InjectMocks
    private CreateTransaction createTransaction;

    @Test
    void shouldCreateTransactionWithBRLCurrencyWithoutCallingExchangeRate() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = createTransaction.create(
                userId,
                TransactionType.PURCHASE,
                BigDecimal.valueOf(100),
                "BRL",
                "Food",
                "Lunch"
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getCurrency()).isEqualTo("BRL");
        assertThat(result.getType()).isEqualTo(TransactionType.PURCHASE);
        assertThat(result.getCategory()).isEqualTo("Food");
        assertThat(result.getDescription()).isEqualTo("Lunch");
        assertThat(result.getStatus()).isEqualTo(TransactionStatus.PENDING);

        verify(exchangeRateGateway, never())
                .getRateToBRL(any(), any());

        verify(repository, times(1)).save(any(Transaction.class));
        verify(publisher, times(1)).publish(any(Transaction.class));
    }

    @Test
    void shouldConvertAmountWhenCurrencyIsNotBRL() {
        // Arrange
        UUID userId = UUID.randomUUID();
        BigDecimal rate = BigDecimal.valueOf(5);
        BigDecimal originalAmount = BigDecimal.valueOf(100);

        when(exchangeRateGateway.getRateToBRL(
                eq("USD"),
                any(LocalDate.class)
        )).thenReturn(rate);

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction result = createTransaction.create(
                userId,
                TransactionType.DEPOSIT,
                originalAmount,
                "USD",
                "Salary",
                "Monthly salary"
        );

        // Assert
        assertThat(result.getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(500));

        assertThat(result.getCurrency()).isEqualTo("BRL");

        verify(exchangeRateGateway, times(1))
                .getRateToBRL(eq("USD"), any(LocalDate.class));

        verify(repository, times(1)).save(any(Transaction.class));
        verify(publisher, times(1)).publish(any(Transaction.class));
    }

    @Test
    void shouldPublishEventAfterSavingTransaction() {
        // Arrange
        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction transaction = createTransaction.create(
                UUID.randomUUID(),
                TransactionType.TRANSFER,
                BigDecimal.valueOf(50),
                "BRL",
                "Transfer",
                "Pix"
        );

        // Assert
        verify(repository).save(transaction);
        verify(publisher).publish(transaction);
    }
}