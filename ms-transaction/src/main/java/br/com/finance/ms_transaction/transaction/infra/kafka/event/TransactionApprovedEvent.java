package br.com.finance.ms_transaction.transaction.infra.kafka.event;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionApprovedEvent (
        UUID transactionId,
        BigDecimal amount
){}
