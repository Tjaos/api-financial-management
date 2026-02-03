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
        System.out.println("Processando transação...");

        if(event.getAmount() == null || event.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            log.warn("Transação a ser rejeitada {}", event.getTransactionId());
            kafkaTemplate.send(
                    "transaction.dlq",
                    event.getTransactionId().toString(),
                    event
            );
            return;
        }

        kafkaTemplate.send(
                "transaction.approved",
                event.getTransactionId().toString(),
                event
        );

        log.info("Transação a ser aprovada {}", event.getTransactionId());
    }
}
