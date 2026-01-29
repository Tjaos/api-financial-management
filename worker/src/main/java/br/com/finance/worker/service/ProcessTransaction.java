package br.com.finance.worker.service;

import br.com.finance.worker.dto.TransactionEventDto;

import java.math.BigDecimal;

public class ProcessTransaction {
    public void process(TransactionEventDto event) {

        System.out.println("Processando transação...");
        System.out.println("Amount = " + event.getAmount());

        if(event.getAmount() == null || event.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException(
                    "Valor inadequado: " + event.getAmount()
            );
        }

        System.out.println("Transação processada com sucesso: "
                + event.getTransactionId());

    }
}
