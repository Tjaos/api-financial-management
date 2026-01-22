package br.com.finance.ms_user.user.application.services;

import br.com.finance.ms_user.user.domain.entities.user.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserSpreadsheetParserTest {

    @Test
    void shouldParseValidSpreadsheetAndReturnUsers() throws Exception {
        // Arrange: cria planilha em memória
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("name");
        header.createCell(1).setCellValue("email");
        header.createCell(2).setCellValue("password");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Thiago");
        row1.createCell(1).setCellValue("thiago@email.com");
        row1.createCell(2).setCellValue("123456");

        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Maria");
        row2.createCell(1).setCellValue("maria@email.com");
        row2.createCell(2).setCellValue("654321");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        UserSpreadsheetParser parser = new UserSpreadsheetParser();

        // Act
        List<User> users = parser.parse(inputStream);

        // Assert
        assertThat(users).hasSize(2);

        assertThat(users.get(0).getName()).isEqualTo("Thiago");
        assertThat(users.get(0).getEmail()).isEqualTo("thiago@email.com");
        assertThat(users.get(0).getPassword()).isEqualTo("123456");

        assertThat(users.get(1).getName()).isEqualTo("Maria");
        assertThat(users.get(1).getEmail()).isEqualTo("maria@email.com");
        assertThat(users.get(1).getPassword()).isEqualTo("654321");
    }

    @Test
    void shouldIgnoreRowsWithMissingFields() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("name");
        header.createCell(1).setCellValue("email");
        header.createCell(2).setCellValue("password");

        Row invalidRow = sheet.createRow(1);
        invalidRow.createCell(0).setCellValue("Sem Email");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        UserSpreadsheetParser parser = new UserSpreadsheetParser();

        // Act
        List<User> users = parser.parse(inputStream);

        // Assert
        assertThat(users).isEmpty();
    }
}