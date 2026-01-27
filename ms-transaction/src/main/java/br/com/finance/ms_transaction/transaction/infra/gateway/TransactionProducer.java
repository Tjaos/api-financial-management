package br.com.finance.ms_transaction.transaction.infra.gateway;

import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

public interface TransactionProducer {
    void send(Transaction transaction);
}
