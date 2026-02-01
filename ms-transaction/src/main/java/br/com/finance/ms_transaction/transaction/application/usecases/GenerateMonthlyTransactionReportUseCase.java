package br.com.finance.ms_transaction.transaction.application.usecases;

import br.com.finance.ms_transaction.transaction.application.dto.MonthlyTransactionReport;
import br.com.finance.ms_transaction.transaction.application.gateways.TransactionReportGateway;
import br.com.finance.ms_transaction.transaction.domain.entities.transaction.Transaction;
import br.com.finance.ms_transaction.transaction.domain.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class GenerateMonthlyTransactionReportUseCase {

    private final TransactionReportGateway gateway;

    public GenerateMonthlyTransactionReportUseCase(TransactionReportGateway gateway) {
        this.gateway = gateway;
    }

    public MonthlyTransactionReport execute(UUID userId, Integer month, Integer year) {

        YearMonth yearMonth = resolveYearMonth(month, year);

        Instant start = yearMonth.atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        Instant end = yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        List<Transaction> transactions =
                gateway.findApprovedByUserIdAndStatusAndCreatedAtBetween(
                        userId,
                        TransactionStatus.APPROVED,
                        start,
                        end
                );

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Transaction tx : transactions) {
            if (tx.isExpense()) {
                expense = expense.add(tx.getAmount());
            } else {
                income = income.add(tx.getAmount());
            }
        }

        return new MonthlyTransactionReport(
                yearMonth.getMonthValue(),
                yearMonth.getYear(),
                income,
                expense,
                income.subtract(expense),
                transactions.stream()
                        .map(tx -> new MonthlyTransactionReport.Item(
                                tx.getId(),
                                tx.getType(),
                                tx.getAmount(),
                                tx.getCategory(),
                                tx.getDescription(),
                                tx.getStatus(),
                                tx.getCreatedAt()
                        ))
                        .toList()
        );
    }

    private YearMonth resolveYearMonth(Integer month, Integer year) {
        if (month == null || year == null) {
            return YearMonth.now();
        }
        return YearMonth.of(year, month);
    }
}
