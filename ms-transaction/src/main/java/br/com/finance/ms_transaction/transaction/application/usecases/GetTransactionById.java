package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.util.UUID;

public class GetTransactionById {

    private final TransactionRepository repository;

    public GetTransactionById(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction getTransaction(UUID userId, UUID transactionId) {

        Transaction transaction = repository.findById(transactionId).orElseThrow(
                () -> new RuntimeException("Transação não encontrada")
        );

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Acesso negado!");
        }
        return transaction;
    }


}
