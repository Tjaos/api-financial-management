package br.com.finance.ms_transaction.transaction.application.gateways;

import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;

import java.time.Instant;
import java.util.List;

public interface TransactionReportGateway {
    List<Transaction> findApprovedByPeriod(Instant start, Instant end);
}
