package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.util.UUID;

public class ApproveTransaction {
    private final TransactionRepository transactionRepository;

    public ApproveTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction approve(UUID transactionId, UUID userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Acesso negado! Usuário não autorizado a aprovar esta transação.");
        }

        transaction.approve();

        return transactionRepository.save(transaction);
    }
}
