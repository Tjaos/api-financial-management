package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.util.UUID;

public class RejectTransaction {

    private final TransactionRepository transactionRepository;

    public RejectTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction reject(UUID transactionId, UUID userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Acesso negado! Usuário não autorizado a rejeitar esta transação.");
        }

        transaction.reject();

        return transactionRepository.save(transaction);
    }
}
