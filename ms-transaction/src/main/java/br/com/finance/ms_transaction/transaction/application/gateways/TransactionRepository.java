package br.com.finance.ms_transaction.transaction.application.gateways;

import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(UUID id);

    List<Transaction> findByUserId(UUID userId);

    void deleteById(UUID id);
}
