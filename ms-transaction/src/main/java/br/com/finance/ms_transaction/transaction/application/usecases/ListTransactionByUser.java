package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.util.List;
import java.util.UUID;

public class ListTransactionByUser {
    private final TransactionRepository repository;

    public ListTransactionByUser(TransactionRepository transactionRepository) {
        this.repository = transactionRepository;
    }

    public List<Transaction> list(UUID userId) {
        return repository.findByUserId(userId);
    }

}
