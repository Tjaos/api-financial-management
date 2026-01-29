package br.com.finance.worker.kafka;

import br.com.finance.worker.service.ProcessTransaction;
import br.com.finance.worker.dto.TransactionEventDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-worker-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, TransactionEventDto> record) {

        ProcessTransaction processTransaction = new ProcessTransaction();

            TransactionEventDto event = record.value();

            System.out.println("Processando transação: " + event.getTransactionId());

            processTransaction.process(event);
    }



}
