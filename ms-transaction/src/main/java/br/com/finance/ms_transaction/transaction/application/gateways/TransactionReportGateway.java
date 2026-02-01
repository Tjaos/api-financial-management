package br.com.finance.ms_transaction.transaction.application.gateways;

import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionReportGateway {
    List<Transaction> findApprovedByUserIdAndStatusAndCreatedAtBetween(
            UUID userId,
            TransactionStatus status,
            Instant start,
            Instant end
    );
}
