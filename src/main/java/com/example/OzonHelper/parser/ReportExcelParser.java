package com.example.OzonHelper.parser;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReportExcelParser {
    public List<List<String>> readCSV(Path path) throws IOException, CsvValidationException {
        List<List<String>> result = new ArrayList<>();

        try (InputStream input = Files.newInputStream(path);
             Workbook workbook = new XSSFWorkbook(input)) {


            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(2);
            int columnCount = headerRow.getLastCellNum();
            System.out.println(columnCount);
            DataFormatter formatter = new DataFormatter();

            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                List<String> values = new ArrayList<>();

                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    Cell cell = row.getCell(
                            columnIndex,
                            Row.MissingCellPolicy.CREATE_NULL_AS_BLANK
                    );
                    values.add(formatter.formatCellValue(cell));
                }
                result.add(values);
            }
        }
        return result;
    }
}
