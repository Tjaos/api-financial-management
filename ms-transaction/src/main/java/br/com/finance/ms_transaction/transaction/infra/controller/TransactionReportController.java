package br.com.finance.ms_transaction.transaction.infra.controller;

import br.com.finance.ms_transaction.transaction.application.dto.MonthlyTransactionReport;
import br.com.finance.ms_transaction.transaction.application.usecases.GenerateMonthlyTransactionReportUseCase;
import br.com.finance.ms_transaction.transaction.infra.dto.MonthlyReportResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions/reports")
public class TransactionReportController {

    private final GenerateMonthlyTransactionReportUseCase generateReport;

    public TransactionReportController(GenerateMonthlyTransactionReportUseCase generateReport) {
        this.generateReport = generateReport;
    }


    @GetMapping
    public MonthlyTransactionReport getReport(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ){
        var report = generateReport.execute(month, year);

        return new MonthlyTransactionReport(
                report.month(),
                report.year(),
                report.totalIncome(),
                report.totalExpense(),
                report.balance(),
                report.items().stream()
                        .map(i -> new MonthlyTransactionReport.Item(
                                i.id(),
                                i.type(),
                                i.amount(),
                                i.category(),
                                i.description(),
                                i.status(),
                                i.createdAt()
                        ))
                        .toList()
        );


    }

}
