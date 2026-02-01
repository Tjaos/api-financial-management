package br.com.finance.worker.kafka;

import br.com.finance.worker.dto.TransactionEventDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionDlqConsumer {

    @KafkaListener(
            topics = "transaction.dql",
            groupId = "transaction-dql-group"
    )
    public void consumeDql(ConsumerRecord<String, Object> record) {
        System.err.println("DLQ - Mensagem recebida: " + record.value());
    }

}
