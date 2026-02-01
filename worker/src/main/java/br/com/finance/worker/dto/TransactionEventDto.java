package br.com.finance.worker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionEventDto {

    private UUID transactionId;
    private UUID userId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String type;

    public TransactionEventDto() {
    }

}
