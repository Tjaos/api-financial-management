package br.com.finance.ms_transaction.transaction.infra.kafka.consumer;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.events.transaction.TransactionEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionApprovedConsumer {

    private final TransactionRepository repository;

    public TransactionApprovedConsumer(TransactionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "transaction.approved",
            groupId = "ms-transaction-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(TransactionEventDto event) {
        System.out.println("Evento aprovado recebido: " + event.getTransactionId());

        Transaction transaction = repository.findById(event.getTransactionId())
                .orElseThrow();

        transaction.approve();

        repository.save(transaction);

        System.out.println(
                "Transação aprovada: " + transaction.getId()
        );
    }
}