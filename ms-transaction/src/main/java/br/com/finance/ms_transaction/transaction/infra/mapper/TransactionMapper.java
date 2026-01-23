package br.com.finance.ms_transaction.transaction.infra.mapper;

import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.persistence.TransactionEntity;

public class TransactionMapper {

    public static TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.getId(),
                transaction.getUserId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }

    public static Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
