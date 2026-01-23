package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.util.UUID;

public class DeleteTransaction {

    private final TransactionRepository transactionRepository;

    public DeleteTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void delete(UUID userId, UUID transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Acesso negado!");
        }

        if (!transaction.isPending()) {
            throw new RuntimeException("Apenas transações pendentes podem ser deletadas");
        }

        transactionRepository.deleteById(transactionId);
    }
}
