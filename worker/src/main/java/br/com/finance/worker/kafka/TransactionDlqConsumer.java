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
    public void consumeDql(ConsumerRecord<String, TransactionEventDto> record) {

        System.err.println("DQL - Mensagem recebida:");
        System.err.println("DQL - Key: " + record.key());
        System.err.println("DQL - Payload: " + record.value());

        record.headers().forEach(header ->
                System.err.println(
                        "Header: " + header.key() + " = " + new String(header.value())
                )
        );
    }

}
