package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.ExchangeRateGateway;
import br.com.finance.ms_transaction.transaction.application.gateways.TransactionEventPublisher;
import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateTransaction {

    private final TransactionRepository repository;
    private final TransactionEventPublisher publisher;
    private final ExchangeRateGateway exchangeRateGateway;


    public CreateTransaction(
            TransactionRepository repository,
            TransactionEventPublisher publisher,
            ExchangeRateGateway exchangeRateGateway
    ) {
        this.repository = repository;
        this.publisher = publisher;
        this.exchangeRateGateway = exchangeRateGateway;
    }

    public Transaction create(
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description
    ) {
        BigDecimal amountInBRL = amount;

        if(!"BRL".equalsIgnoreCase(currency)){
            BigDecimal rateToBRL = exchangeRateGateway.getRateToBRL(currency, java.time.LocalDate.now().minus(1, java.time.temporal.ChronoUnit.DAYS));
            amountInBRL = amountInBRL.multiply(rateToBRL);
        }

        Transaction newTransaction = Transaction.create(
                userId,
                type,
                amountInBRL,
                "BRL",
                category,
                description
        );

       Transaction saved =  repository.save(newTransaction);

       publisher.publish(saved);

        return saved;
    }

}
