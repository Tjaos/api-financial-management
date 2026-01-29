package br.com.finance.ms_user.user.application.usecases;

import br.com.finance.ms_user.user.domain.entities.user.User;
import org.apache.poi.ss.usermodel.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserSpreadsheetParser {

    public List<User> parse(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            List<User> users = new ArrayList<>();

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String name = getCellValue(row.getCell(0));
                String email = getCellValue(row.getCell(1));
                String password = getCellValue(row.getCell(2));

                if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
                    continue;
                }

                users.add(new User(name, email, password));
            }

            return users;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar planilha", e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }
}
