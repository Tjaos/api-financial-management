package br.com.finance.ms_transaction.transaction.infra.persistence;

import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByUserId(UUID id);
    List<TransactionEntity> findApprovedByUserIdAndStatusAndCreatedAtBetween(
            UUID userId,
            TransactionStatus status,
            Instant start,
            Instant end
    );
}
