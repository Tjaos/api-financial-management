package br.com.finance.ms_transaction.transaction.application.gateways;

import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

public interface TransactionEventPublisher {

    void publish(Transaction transaction);
}
