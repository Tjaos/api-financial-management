package br.com.finance.worker.kafka;

import br.com.finance.worker.service.ProcessTransactionUseCase;
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
    public void consume(
            ConsumerRecord<String, TransactionEventDto> record) {


        ProcessTransactionUseCase processTransactionUseCase = new ProcessTransactionUseCase();
            TransactionEventDto event = record.value();

            System.out.println("Mensagem recebida:");
            System.out.println("Key: " + record.key());
            System.out.println("Payload: " + event);

            processTransactionUseCase.process(event);
    }



}
