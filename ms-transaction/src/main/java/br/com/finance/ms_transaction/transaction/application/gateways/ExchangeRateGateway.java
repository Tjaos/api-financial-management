package br.com.finance.ms_transaction.transaction.application.gateways;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeRateGateway {

    BigDecimal getRateToBRL(String currency, LocalDate date);
}
