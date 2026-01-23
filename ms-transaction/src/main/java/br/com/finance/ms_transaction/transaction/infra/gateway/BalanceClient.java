package br.com.finance.ms_transaction.transaction.infra.gateway;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceClient {
    boolean hasSufficientBalance(UUID userId, BigDecimal amount);
}
