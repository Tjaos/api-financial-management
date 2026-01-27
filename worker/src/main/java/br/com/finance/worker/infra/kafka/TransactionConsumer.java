package br.com.finance.worker.infra.kafka;

import br.com.finance.worker.infra.dto.TransactionEventDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-worker-group"
    )
    public void consume(ConsumerRecord<String, TransactionEventDto> record) {

        TransactionEventDto event = record.value();

        System.out.println("🔔 Mensagem recebida do Kafka:");
        System.out.println("Key: " + record.key());
        System.out.println("Payload: " + event);

        // Aqui depois:
        // - validar
        // - processar
        // - chamar outros serviços
        // - persistir status
        // - em caso de erro → DLQ
    }
}
