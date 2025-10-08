package com.example.OzonHelper.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReportGenerator {

    public static final String UTF8_BOM = "\uFEFF";

    public Path generateReportCsv(OffsetDateTime periodStart, Map<String, Integer> salesData) throws IOException {
        String fileName = "shimgeReport.csv";
        Path filePath = Paths.get(fileName);

        Writer writer = new OutputStreamWriter(new FileOutputStream(filePath.toFile()), StandardCharsets.UTF_8);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create().setDelimiter(";").build());

        writer.write(UTF8_BOM);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        String headerDateString;

        headerDateString = " " + periodStart.format(formatter);

        csvPrinter.printRecord(headerDateString, "");
        csvPrinter.printRecord();

        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            csvPrinter.printRecord(entry.getKey(), entry.getValue());
        }

        int totalQuantity = salesData.values().stream().mapToInt(Integer::intValue).sum();
        csvPrinter.printRecord("", totalQuantity + " шт");

        csvPrinter.flush();

        return filePath;
    }

    public Path generateReportXlsx(OffsetDateTime periodStart, Map<String, Integer> salesData) throws IOException {
        String fileName = "shimgeReport.xlsx";
        Path filePath = Paths.get(fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Отчет");

            // --- 1. СОЗДАЕМ СТИЛИ И ШРИФТЫ ---
            // Создаем стили один раз, чтобы потом их переиспользовать.

            // Шрифт для заголовка (увеличенный)
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12); // Увеличиваем размер шрифта
            headerFont.setBold(true); // Можно сделать его жирным

            // Стиль для заголовка (центрированный, с увеличенным шрифтом)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER); // <-- ЦЕНТРИРОВАНИЕ
            headerStyle.setFont(headerFont);

            // Стиль для ячеек с данными (с границами)
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setBorderTop(BorderStyle.THIN);    // <-- ДОБАВЛЯЕМ ГРАНИЦЫ
            dataCellStyle.setBorderBottom(BorderStyle.THIN);
            dataCellStyle.setBorderLeft(BorderStyle.THIN);
            dataCellStyle.setBorderRight(BorderStyle.THIN);

            // Стиль для итоговой ячейки (границы + выравнивание по правому краю)
            CellStyle totalCellStyle = workbook.createCellStyle();
            totalCellStyle.cloneStyleFrom(dataCellStyle); // Копируем границы из стиля данных
            totalCellStyle.setAlignment(HorizontalAlignment.RIGHT); // Выравниваем по правому краю


            // --- 2. ЗАПОЛНЯЕМ ДАННЫЕ И ПРИМЕНЯЕМ СТИЛИ ---
            int rowNum = 0;

            // Заголовок с датой
            Row headerRow = sheet.createRow(rowNum++);
            Cell headerCell = headerRow.createCell(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            headerCell.setCellValue(periodStart.format(formatter));
            headerCell.setCellStyle(headerStyle); // <-- Применяем стиль заголовка

            // Пустая строка
            rowNum++;

            // Данные о продажах
            for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
                Row dataRow = sheet.createRow(rowNum++);

                Cell articleCell = dataRow.createCell(0);
                articleCell.setCellValue(entry.getKey());
                articleCell.setCellStyle(dataCellStyle); // <-- Применяем стиль данных

                Cell quantityCell = dataRow.createCell(1);
                quantityCell.setCellValue(entry.getValue());
                quantityCell.setCellStyle(dataCellStyle); // <-- Применяем стиль данных
            }

            // Итоговая строка
            int totalQuantity = salesData.values().stream().mapToInt(Integer::intValue).sum();
            Row totalRow = sheet.createRow(rowNum);

            // Создаем пустую ячейку слева, чтобы граница была сплошной
            Cell emptyTotalCell = totalRow.createCell(0);
            emptyTotalCell.setCellStyle(dataCellStyle);

            Cell totalCell = totalRow.createCell(1);
            totalCell.setCellValue(totalQuantity + " шт");
            totalCell.setCellStyle(totalCellStyle); // <-- Применяем стиль для итогов


            // --- 3. НАСТРАИВАЕМ ШИРИНУ КОЛОНОК ---
            sheet.setColumnWidth(0, 40 * 256); // Увеличим ширину для длинных названий
            sheet.setColumnWidth(1, 10 * 256);


            // Записываем все в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
                workbook.write(fileOut);
            }
        }

        System.out.println("Excel-отчет успешно сгенерирован: " + filePath.toAbsolutePath());
        return filePath;
    }
}
