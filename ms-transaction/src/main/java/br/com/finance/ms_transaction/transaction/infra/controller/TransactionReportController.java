package br.com.finance.ms_transaction.transaction.infra.controller;

import br.com.finance.ms_transaction.transaction.application.dto.MonthlyTransactionReport;
import br.com.finance.ms_transaction.transaction.application.usecases.GenerateMonthlyTransactionReportUseCase;
import br.com.finance.ms_transaction.transaction.infra.service.MonthlyTransactionReportExcelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions/reports")
public class TransactionReportController {

    private final GenerateMonthlyTransactionReportUseCase generateReport;
    private final MonthlyTransactionReportExcelService excelService;

    private final JwtUserExtractor jwtUserExtractor;

    public TransactionReportController(
            GenerateMonthlyTransactionReportUseCase generateReport, MonthlyTransactionReportExcelService excelService,
            JwtUserExtractor jwtUserExtractor
    ) {
        this.generateReport = generateReport;
        this.excelService = excelService;
        this.jwtUserExtractor = jwtUserExtractor;
    }

    @GetMapping
    public MonthlyTransactionReport getReport(
            @RequestParam(required = false, name = "month") Integer month,
            @RequestParam(required = false, name = "year") Integer year,
            @RequestHeader("Authorization") String authorization
    ) {
        UUID userId = jwtUserExtractor.extractUserId(authorization);
        return generateReport.execute(userId, month, year);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam(required = false, name = "month") Integer month,
            @RequestParam(required = false, name = "year") Integer year,
            @RequestHeader("Authorization") String authorization
    ) {
        UUID userId = jwtUserExtractor.extractUserId(authorization);

        MonthlyTransactionReport report =
                generateReport.execute(userId, month, year);

        byte[] file =
                excelService.generate(report);

        String filename = String.format(
                "monthly-transaction-report-%d-%02d.xlsx",
                report.year(),
                report.month()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }
}
