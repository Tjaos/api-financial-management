package br.com.finance.ms_transaction.transaction.infra.kafka;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.kafka.event.TransactionApprovedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionApprovedConsumer {

    private final TransactionRepository repository;

    public TransactionApprovedConsumer(TransactionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "ms-transaction-group"
    )
    public void consume(TransactionApprovedEvent event) {

        Transaction transaction = repository.findById(event.transactionId())
                .orElseThrow();

        transaction.approve();

        repository.save(transaction);

        System.out.println(
                "Transação aprovada: " + transaction.getId()
        );
    }
}