package br.com.finance.worker.service;

import br.com.finance.worker.dto.TransactionEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProcessTransaction {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProcessTransaction(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(TransactionEventDto event) {
        System.out.println("Processando transação...");

        if(event.getAmount() == null || event.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException(
                    "Valor inválido: " + event.getAmount()
            );
        }
        TransactionEventDto approvedEvent = new TransactionEventDto();
        approvedEvent.setTransactionId(event.getTransactionId());
        approvedEvent.setUserId(event.getUserId());
        approvedEvent.setAmount(event.getAmount());
        approvedEvent.setStatus("APPROVED");
        kafkaTemplate.send("transaction.requested", approvedEvent);
        System.out.println("Transação aprovada: " + event.getTransactionId());


    }
}
