package br.com.finance.worker.service;

import br.com.finance.events.transaction.TransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ProcessTransaction {

    private final KafkaTemplate<String, TransactionEventDto> kafkaTemplate;

    public ProcessTransaction(KafkaTemplate<String, TransactionEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(TransactionEventDto event) {

        if(event.getAmount() == null || event.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            log.warn("Transação a ser rejeitada {}", event.getTransactionId());
            kafkaTemplate.send(
                    "transaction.dlq",
                    event.getTransactionId().toString(),
                    event
            );
            log.info("Transação rejeitada publicada com sucesso no tópico transaction.dlq");
            return;
        }

        kafkaTemplate.send(
                "transaction.approved",
                event.getTransactionId().toString(),
                event
        )
                        .whenComplete((result, ex) -> {
                            if(ex != null){
                                log.error("Falha ao publicar transação aprovada no evento", ex);
                            } else{
                                log.info("Transação aprovada publicada com sucesso no tópico {} na partição {} com offset {}",
                                        result.getRecordMetadata().topic(),
                                        result.getRecordMetadata().partition(),
                                        result.getRecordMetadata().offset());
                            }
                        });
    }
}
