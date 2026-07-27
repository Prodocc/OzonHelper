package com.example.OzonHelper.client;

import com.example.OzonHelper.domain.StockItem;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

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

    public void writeDailyReportItems(String spreadsheetId, String sheetName, List<StockItem> items) throws Exception {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDate today = LocalDate.now(zoneId);

        String skuRange = sheetName + "!C:C";

        Map<String, Integer> skuToRowMap = readSkuToRowMap(spreadsheetId, skuRange);

        SheetColumnRange sheetColumnRange = buildDailySheetColumnRange(today, sheetName);

        Function<StockItem, List<Object>> dailyValuesMapper = stockItem -> Arrays.asList(
                stockItem.getSellsForYesterday(),
                stockItem.getAvailableStock(),
                stockItem.getInTransitStock(),
                stockItem.getSellsForLastThreeWeeks());

        List<ValueRange> dataRanges = buildDataRanges(items, skuToRowMap, sheetColumnRange, dailyValuesMapper);

        if (dataRanges.isEmpty()) {
            System.out.println("Нет данных для записи в строки");
            return;
        }

        writeDataRanges(spreadsheetId, dataRanges);

        System.out.println("Данные по товарам успешно записаны: " + dataRanges.size() + " строк");
    }

    public void writeWeeklyReportItems(String spreadsheetId, String sheetName, List<StockItem> items, String weeklyColumnRange) throws IOException {
        String skuRange = sheetName + "!B:B";

        Map<String, Integer> skuToRowMap = readSkuToRowMap(spreadsheetId, skuRange);

        SheetColumnRange sheetColumnRange = buildWeeklySheetColumnRange(weeklyColumnRange, sheetName);

        Function<StockItem, List<Object>> weeklyValuesMapper = stockItem -> Arrays.asList(
                stockItem.getSellsForLastWeek(),
                stockItem.getAvailableStock(),
                stockItem.getInTransitStock());

        List<ValueRange> dataRanges = buildDataRanges(items, skuToRowMap, sheetColumnRange, weeklyValuesMapper);

        if (dataRanges.isEmpty()) {
            System.out.println("Нет данных для записи в строки");
            return;
        }

        writeDataRanges(spreadsheetId, dataRanges);

        System.out.println("Данные по товарам успешно записаны: " + dataRanges.size() + " строк");
    }

    public List<List<Object>> readTable(String spreadsheetId, String range) throws IOException {
        return sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute().getValues();
    }

    private Map<String, Integer> buildSkuToRowMap(List<List<Object>> skuValues) {
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

        return skuToRow;
    }

    private void writeDataRanges(String spreadsheetId, List<ValueRange> dataRanges) throws IOException {
        BatchUpdateValuesRequest batchDataRequest = new BatchUpdateValuesRequest()
                .setValueInputOption("RAW")
                .setData(dataRanges);


        sheetsService.spreadsheets()
                .values()
                .batchUpdate(spreadsheetId, batchDataRequest)
                .execute();
    }

    private List<List<Object>> readRangeValues(String spreadsheetId, String skuRange) throws IOException {
        var skuResponse = sheetsService.spreadsheets().values()
                .get(spreadsheetId, skuRange)
                .execute();

        return skuResponse.getValues();
    }

    public Map<String, Integer> readSkuToRowMap(String spreadsheetId, String skuRange) throws IOException {
        List<List<Object>> skuValues = readRangeValues(spreadsheetId, skuRange);
        if (skuValues == null || skuValues.isEmpty()) {
            System.out.println("Столбец SKU пуст");
            return Map.of();
        }

        return buildSkuToRowMap(skuValues);
    }

    private List<ValueRange> buildDataRanges(List<StockItem> items, Map<String, Integer> skuToRowMap,
                                             SheetColumnRange sheetColumnRange,
                                             Function<StockItem, List<Object>> valuesMapper) {
        List<ValueRange> dataRanges = new ArrayList<>();

        for (StockItem item : items) {
            Integer rowNum = skuToRowMap.get(item.getSku());
            if (rowNum == null) {
                continue;
            }

            String range = sheetColumnRange.buildRangeForRow(rowNum);

            List<Object> valuesRow = valuesMapper.apply(item);

            ValueRange valueRange = new ValueRange()
                    .setRange(range)
                    .setValues(Collections.singletonList(valuesRow));

            dataRanges.add(valueRange);
        }

        return dataRanges;
    }

    private SheetColumnRange buildWeeklySheetColumnRange(String weeklyColumnRange, String sheetName) {
        String startColLetter = weeklyColumnRange.split(":")[0];
        String endColLetter = weeklyColumnRange.split(":")[1];

        return new SheetColumnRange(sheetName, startColLetter, endColLetter);
    }

    private SheetColumnRange buildDailySheetColumnRange(LocalDate today, String sheetName) {
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int dayIndex = dayOfWeek.getValue() - 1; // Monday=0

        // Для Пн (0): старт E (4), конец H (7). Для Вт (1): старт I (8), конец L (11)
        int startColIndex = 4 + (dayIndex * 4);
        int endColIndex = startColIndex + 3;

        String startColLetter = colIndexToLetter(startColIndex);
        String endColLetter = colIndexToLetter(endColIndex);

        return new SheetColumnRange(sheetName, startColLetter, endColLetter);
    }

    private record SheetColumnRange(String sheetName, String startColLetter, String endColLetter) {
        private String buildRangeForRow(int rowNum) {
            return sheetName +
                    "!" + startColLetter + rowNum +
                    ":" + endColLetter + rowNum;
        }
    }

}
