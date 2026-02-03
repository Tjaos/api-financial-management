package br.com.finance.worker.kafka;

import br.com.finance.worker.service.ProcessTransaction;
import br.com.finance.events.transaction.TransactionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionConsumer {

    private final ProcessTransaction processTransaction;

    public TransactionConsumer(ProcessTransaction processTransaction) {
        this.processTransaction = processTransaction;
    }

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-worker-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, TransactionEventDto> record) {

        TransactionEventDto event = record.value();
        if(event == null){
            log.warn("Evento nulo recebido no consumidor de transação");
            return;
        }

        log.info("Processando transação: {}", event.getTransactionId());

        try{
            processTransaction.process(event);
        }catch (Exception ex){
            log.error("Erro ao processar a transação: {}", event.getTransactionId(), ex);
            throw ex;
        }


    }



}
