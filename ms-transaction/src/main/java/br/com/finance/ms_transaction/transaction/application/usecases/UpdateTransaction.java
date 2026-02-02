package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateTransaction {

    private final TransactionRepository repository;

    public UpdateTransaction(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction update(
            UUID transactionId,
            UUID userId,
            TransactionType type,
            BigDecimal amount,
            String currency,
            String category,
            String description
    ) {
        Transaction transaction = repository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Acesso negado!");
        }
        if(transaction.getStatus() != null && !transaction.getStatus().name().equals("PENDING")) {
            throw new RuntimeException("Apenas transções pendentes podem ser atualizadas!");
        }
        Transaction updated = new Transaction(
                transaction.getId(),
                transaction.getUserId(),
                type != null ? type : transaction.getType(),
                amount != null ? amount : transaction.getAmount(),
                currency != null ? currency : transaction.getCurrency(),
                category != null ? category : transaction.getCategory(),
                description != null ? description : transaction.getDescription(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
        return repository.save(updated);
    }


}
