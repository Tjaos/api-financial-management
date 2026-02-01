package br.com.finance.worker.kafka;

import br.com.finance.worker.service.ProcessTransaction;
import br.com.finance.worker.dto.TransactionEventDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProcessTransaction processTransaction;

    public TransactionConsumer(KafkaTemplate<String, Object> kafkaTemplate, ProcessTransaction processTransaction) {
        this.kafkaTemplate = kafkaTemplate;
        this.processTransaction = processTransaction;
    }

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-worker-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, TransactionEventDto> record) {

        ProcessTransaction processTransaction = new ProcessTransaction(kafkaTemplate);
        TransactionEventDto event = record.value();
        System.out.println("Processando transação: " + event.getTransactionId());
        processTransaction.process(event);


    }



}
