package br.com.finance.ms_transaction.transaction.infra.gateway;

import java.math.BigDecimal;

public interface ExchangeRateClient {
    BigDecimal convertToBRL(BigDecimal amount, String currency);
}

