package br.com.finance.ms_transaction.transaction.infra.kafka.consumer;

import br.com.finance.events.transaction.TransactionEventDto;
import br.com.finance.ms_transaction.transaction.application.gateways.TransactionRepository;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionDlqConsumer {

    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;

    public TransactionDlqConsumer(
            TransactionRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "transaction.dlq",
            groupId = "ms-transaction-dlq-group",
            containerFactory = "dlqKafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record) {

        log.error("""
                🚨 DLQ MESSAGE 🚨
                Topic: {}
                Partition: {}
                Offset: {}
                Key: {}
                Payload (raw): {}
                """,
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value()
        );

        try {
            TransactionEventDto event =
                    objectMapper.readValue(record.value(), TransactionEventDto.class);

            Transaction transaction = repository.findById(event.getTransactionId())
                    .orElseThrow();

            transaction.reject();
            repository.save(transaction);

            log.info("Transação rejeitada com sucesso: {}", transaction.getId());

        } catch (Exception e) {
            log.error("Erro ao processar mensagem da DLQ", e);
        }
    }
}