package br.com.finance.ms_transaction.transaction.infra.kafka.consumer;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.events.transaction.TransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
    public void consume(ConsumerRecord<String, TransactionEventDto> record) {

        TransactionEventDto event = record.value();
        if(event == null){
            log.warn("Evento nulo recebido no consumidor de transação");
            return;
        }

        log.info("Evento transaction.approved recebido: {}", event.getTransactionId());

        Transaction transaction;
        transaction = repository.findById(event.getTransactionId())
                .orElseThrow();
        try {
            transaction.approve();
            repository.save(transaction);
        } catch (Exception e) {
            log.error("Erro ao processar transação aprovada: {}", e.getMessage());
            return;
        }

        log.info("Transação aprovada: {}", transaction.getId());
    }

}