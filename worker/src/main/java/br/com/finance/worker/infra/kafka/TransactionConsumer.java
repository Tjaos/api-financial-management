package br.com.finance.worker.infra.kafka;

import br.com.finance.worker.infra.dto.TransactionEventDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionConsumer {

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-worker-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            ConsumerRecord<String, TransactionEventDto> record) {

            TransactionEventDto event = record.value();

            System.out.println("Mensagem recebida:");
            System.out.println("Key: " + record.key());
            System.out.println("Payload: " + event);

            process(event);
    }

    private void process(TransactionEventDto event) {
        System.out.println(">>> ENTROU NO PROCESS");
        System.out.println(">>> AMOUNT = " + event.getAmount());

        throw new IllegalArgumentException("FORÇANDO ERRO DLQ");
    }


}
