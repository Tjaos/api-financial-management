package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import br.com.finance.ms_transaction.transaction.infra.gateway.TransactionProducer;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateTransaction {

    private final TransactionRepository repository;
    private final TransactionProducer producer;


    public CreateTransaction(TransactionRepository repository, TransactionProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public Transaction create(
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description
    ) {

        Transaction newTransaction = Transaction.create(
                userId,
                type,
                amount,
                currency,
                category,
                description
        );

       Transaction saved =  repository.save(newTransaction);

       producer.send(saved);

        return saved;
    }

}
