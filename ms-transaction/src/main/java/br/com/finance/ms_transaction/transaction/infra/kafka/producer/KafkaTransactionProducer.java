package br.com.finance.ms_transaction.transaction.infra.kafka.producer;

import br.com.finance.ms_transaction.transaction.application.gateways.TransactionEventPublisher;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.infra.kafka.event.TransactionApprovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTransactionProducer implements TransactionEventPublisher {

    private static final String TOPIC = "transaction.requested";

    private final KafkaTemplate<String, TransactionApprovedEvent> kafkaTemplate;

    public KafkaTransactionProducer(KafkaTemplate<String, TransactionApprovedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void publish(Transaction transaction) {
        TransactionApprovedEvent event  = new TransactionApprovedEvent(
                transaction.getId(),
                transaction.getAmount()
        );

        log.info("Publicando transaction o evento {} para o tópico {}", event, TOPIC);

        kafkaTemplate.send(TOPIC, transaction.getId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null){
                        log.error("Falha ao publicar transação no evento", ex);
                    } else{
                        log.info("Transação publicada com sucesso no tópico {} na partição {} com offset {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });

    }

}
