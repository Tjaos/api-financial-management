package br.com.finance.ms_transaction.transaction.infra.worker;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.dto.TransactionEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionWorker {

    private final TransactionRepository repository;
    private final KafkaTemplate<String, TransactionEventDto> kafkaTemplate;

    public TransactionWorker(TransactionRepository repository,
                             KafkaTemplate<String, TransactionEventDto> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "transaction.requested", groupId = "transaction-worker")
    public void consume(TransactionEventDto event) {

        try {
            Transaction transaction = repository.findById(event.transactionId())
                    .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + event.transactionId()));


            if (event.amount().compareTo(new BigDecimal("10000")) < 0) {
                transaction.approve();
            } else {
                transaction.reject();
            }

            repository.save(transaction);

        } catch (Exception e) {
            kafkaTemplate.send("transaction.dlq", event.transactionId().toString(), event);
        }
    }
}
