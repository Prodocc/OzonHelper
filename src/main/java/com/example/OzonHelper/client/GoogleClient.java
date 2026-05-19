package com.example.OzonHelper.client;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleClient {
    private final Sheets sheetsService;

    public GoogleClient(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    public void writeTable(List<List<Object>> rawData, String spreadSheetId, String range) throws IOException {
        System.out.println(rawData);
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

}
