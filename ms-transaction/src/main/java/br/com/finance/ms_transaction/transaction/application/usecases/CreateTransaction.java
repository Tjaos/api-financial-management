package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CreateTransaction {

    private final TransactionRepository repository;


    public CreateTransaction(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction create(
            UUID id,
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description
    ) {

        Transaction newTransaction = new Transaction(
                id,
                userId,
                type,
                amount,
                currency,
                category,
                description,
                TransactionStatus.PENDING,
                Instant.now()

        );

        return repository.save(newTransaction);
    }
}
