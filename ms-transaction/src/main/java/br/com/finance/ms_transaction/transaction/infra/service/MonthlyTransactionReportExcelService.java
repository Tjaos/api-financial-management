package br.com.finance.ms_transaction.transaction.infra.service;

import br.com.finance.ms_transaction.transaction.application.dto.MonthlyTransactionReport;
import br.com.finance.ms_transaction.transaction.infra.dto.TransactionResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;

@Service
public class MonthlyTransactionReportExcelService {

    public byte[] generate(MonthlyTransactionReport report) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Análise Mensal de Transações");

            int rowIndex = 0;

            // ===== HEADER =====
            Row header = sheet.createRow(rowIndex++);
            String[] columns = {
                    "ID", "Type", "Amount", "Category",
                    "Description", "Status", "Created At"
            };

            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== DATA =====
            for (MonthlyTransactionReport.Item tx : report.items()) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(tx.id().toString());
                row.createCell(1).setCellValue(tx.type().name());
                row.createCell(2).setCellValue(tx.amount().doubleValue());
                row.createCell(3).setCellValue(tx.category());
                row.createCell(4).setCellValue(tx.description());
                row.createCell(5).setCellValue(tx.status().name());
                row.createCell(6).setCellValue(
                        tx.createdAt()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                                .toString()
                );
            }

            // ===== SUMMARY =====
            rowIndex++;
            rowIndex = createSummaryRow(sheet, rowIndex, "Total Income", report.totalIncome().doubleValue());
            rowIndex = createSummaryRow(sheet, rowIndex, "Total Expense", report.totalExpense().doubleValue());
            createSummaryRow(sheet, rowIndex, "Balance", report.balance().doubleValue());

            // Auto-size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel report", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private int createSummaryRow(Sheet sheet, int rowIndex, String label, double value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(2).setCellValue(value);
        return rowIndex + 1;
    }
}
