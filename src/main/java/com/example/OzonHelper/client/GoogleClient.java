package com.example.OzonHelper.client;

import com.example.OzonHelper.domain.StockItem;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.OzonHelper.util.GoogleUtils.colIndexToLetter;

@Component
public class GoogleClient {
    private final Sheets sheetsService;

    public GoogleClient(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    public void writeTable(List<List<Object>> rawData, String spreadSheetId, String range) throws IOException {
        ValueRange body = new ValueRange().setValues(rawData);

        sheetsService.spreadsheets().values()
                .update(spreadSheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public void insertRow(String spreadSheetId, int sheetId, int startIndex, int endIndex) throws IOException {
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest();
        Request request = new Request();
        InsertDimensionRequest dimensionRequest = new InsertDimensionRequest();
        DimensionRange range = new DimensionRange();

        range.setSheetId(sheetId);
        range.setDimension("ROWS");
        range.setStartIndex(startIndex + 1);
        range.setEndIndex(endIndex + 1);

        dimensionRequest.setRange(range);
        dimensionRequest.setInheritFromBefore(false);

        request.setInsertDimension(dimensionRequest);
        batchUpdateRequest.setRequests(Collections.singletonList(request));
        sheetsService.spreadsheets().batchUpdate(spreadSheetId,
                batchUpdateRequest).execute();
    }

    public void setBackgroundColor(String spreadSheetId, int sheetId, GridRange gridRange, Color color) throws IOException {
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest();
        Request request = new Request();
        RepeatCellRequest cellRequest = new RepeatCellRequest();
        CellData cellData = new CellData();
        CellFormat cellFormat = new CellFormat();

        cellFormat.setBackgroundColor(color);
        cellData.setUserEnteredFormat(cellFormat);
        gridRange.setSheetId(sheetId);
        cellRequest.setCell(cellData);
        cellRequest.setRange(gridRange);
        cellRequest.setFields("userEnteredFormat.backgroundColor");
        request.setRepeatCell(cellRequest);

        batchUpdateRequest.setRequests(Collections.singletonList(request));
        sheetsService.spreadsheets().batchUpdate(spreadSheetId,
                batchUpdateRequest).execute();
    }

    public List<List<Object>> fetchFreshData(String spreadSheetId, String range) throws IOException {
        return sheetsService.spreadsheets().values().get(spreadSheetId, range).execute().getValues();
    }

    public List<Sheet> getSheets(String spreadSheetId) throws IOException {
        return sheetsService.spreadsheets().get(spreadSheetId).execute().getSheets();
    }

    public int getSheetIdByTitle(String sheetTitle, String spreadSheetId) throws IOException {
        if (sheetTitle == null || spreadSheetId == null) {
            return -1;
        }
        List<Sheet> sheets = sheetsService.spreadsheets().get(spreadSheetId).execute().getSheets();
        return extractSheetIdFromList(sheets, sheetTitle);
    }

    private int extractSheetIdFromList(List<Sheet> sheets, String sheetTitle) {
        for (Sheet sheet : sheets) {
            if (sheet.getProperties().getTitle().equals(sheetTitle)) {
                return sheet.getProperties().getSheetId();
            }
        }
        return -1;
    }

    public void writeStockItemsByDay(String spreadsheetId, String sheetName, List<StockItem> items) throws Exception {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDate today = LocalDate.now(zoneId);

        // --- 1. Расчет столбцов для текущего дня ---
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int dayIndex = dayOfWeek.getValue() - 1; // Monday=0

        // Для Пн (0): старт E (4), конец H (7). Для Вт (1): старт I (8), конец L (11)
        int startColIndex = 4 + (dayIndex * 4);
        int endColIndex = startColIndex + 3;

        String startColLetter = colIndexToLetter(startColIndex);
        String endColLetter = colIndexToLetter(endColIndex);

        // --- 2. Читаем столбец B (SKU), чтобы найти номера строк ---
        String skuRange = sheetName + "!C:C";
        var skuResponse = sheetsService.spreadsheets().values()
                .get(spreadsheetId, skuRange)
                .execute();

        List<List<Object>> skuValues = skuResponse.getValues();
        if (skuValues == null || skuValues.isEmpty()) {
            System.out.println("Столбец SKU пуст");
            return;
        }

        // Мапа: SKU -> номер строки (1-based, т.к. в API строки начинаются с 1)
        Map<String, Integer> skuToRow = new HashMap<>();
        for (int i = 0; i < skuValues.size(); i++) {
            List<Object> row = skuValues.get(i);
            if (!row.isEmpty()) {
                String sku = row.get(0).toString().trim();
                // Пропускаем пустые и заголовок "SKU"
                if (!sku.isEmpty() && !"SKU".equalsIgnoreCase(sku)) {
                    skuToRow.put(sku, i + 1);
                }
            }
        }

        List<ValueRange> dataRanges = new ArrayList<>();

        for (StockItem item : items) {
            Integer rowNum = skuToRow.get(item.getSku());
            if (rowNum == null) {
                continue;
            }

            String range = sheetName + "!" + startColLetter + rowNum + ":" + endColLetter + rowNum;

            List<Object> valuesRow = Arrays.asList(
                    item.getSellsForYesterday(),
                    item.getAvailableStock(),
                    item.getInTransitStock(),
                    item.getSellsForLastThreeWeeks()
            );

            ValueRange vr = new ValueRange()
                    .setRange(range)
                    .setValues(Collections.singletonList(valuesRow));

            dataRanges.add(vr);
        }

        if (dataRanges.isEmpty()) {
            System.out.println("Нет данных для записи в строки");
            return;
        }

        BatchUpdateValuesRequest batchDataRequest = new BatchUpdateValuesRequest()
                .setValueInputOption("RAW")
                .setData(dataRanges);


        sheetsService.spreadsheets()
                .values()
                .batchUpdate(spreadsheetId, batchDataRequest)
                .execute();

        System.out.println("Данные по товарам успешно записаны: " + dataRanges.size() + " строк");

    }

}
