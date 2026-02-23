package br.com.finance.worker.kafka;

import br.com.finance.worker.service.ProcessTransaction;
import br.com.finance.events.transaction.TransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionConsumer {

    private final ProcessTransaction processTransaction;

    public TransactionConsumer(ProcessTransaction processTransaction) {
        this.processTransaction = processTransaction;
    }

    @KafkaListener(topics = "transaction.requested")
    public void consume(TransactionEventDto event) {

        log.info("Evento recebido: {}", event.getTransactionId());
        processTransaction.process(event);
    }


}
