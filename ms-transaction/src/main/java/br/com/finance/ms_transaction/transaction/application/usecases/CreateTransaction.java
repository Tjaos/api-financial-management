package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateTransaction {

    private final TransactionRepository repository;


    public CreateTransaction(TransactionRepository repository) {
        this.repository = repository;
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

        return repository.save(newTransaction);
    }
}
