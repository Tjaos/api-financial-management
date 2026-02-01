package br.com.finance.ms_transaction.transaction.infra.kafka.event;

import java.util.UUID;

public record TransactionApprovedEvent (
    UUID transactionId
){}
