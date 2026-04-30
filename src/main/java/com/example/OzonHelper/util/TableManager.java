package com.example.OzonHelper.util;

import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.enums.SheetColors;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class TableManager {
    private static final String FBS_LOG_LIST = "fbs-log-id";
    private static final String FBS_LOG_LIST_RANGE = "B1:J500";
    private final String FBS_LOG_LIST_DATE_COLUMN = "B";
    private final String FBS_LOG_LIST_PARTNER_COLUMN = "C";
    private final String FBS_LOG_LIST_ADDRESS_COLUMN = "D";
    private final String FBS_LOG_LIST_MANAGER_COLUMN = "G";
    private final int FBS_ROWS_TO_ADD = 5;
    private final String FBS_STRING_TO_CHECK = "FBS";

    private final Map<String, String> spreadSheetsIds;
    private final Sheets sheetsService;
    private List<List<Object>> fbsLogList;

    public TableManager(GoogleSheetsProperties properties, Sheets sheetsService) {
        this.spreadSheetsIds = properties.getSheets();
        this.sheetsService = sheetsService;
    }

    public void writeTable(List<List<Object>> rowData, String range) throws IOException {
        ValueRange body = new ValueRange().setValues(rowData);

        System.out.println("range = " + range);

        sheetsService.spreadsheets().values()
                .update(spreadSheetsIds.get(FBS_LOG_LIST), range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public void CheckAndWriteFbsLogListPostings() throws IOException {
        String sheetTitle = getFbsLogListTitle();
        String range = getFbsLogListRange(sheetTitle);
        fetchFreshFbsLogListFromGoogle(range);
        SheetScope logListScope = findLogListScope();
        System.out.println(logListScope);
        if (!scopeHasPostings(logListScope)) {
            if (logListScope.isNew()) {
                List<List<Object>> scopeStartData = createScopeStartData();
                setBackgroundColor(getSheetId(sheetTitle), createRowRange(logListScope.getStartIndex(), logListScope.getStartIndex() + 1), SheetColors.FBS_LIST_COLOR_DATE_COLOR.toGoogleColor());
                writeTable(scopeStartData,
                        GoogleUtils.buildRange(
                                sheetTitle,
                                FBS_LOG_LIST_DATE_COLUMN,
                                FBS_LOG_LIST_MANAGER_COLUMN,
                                logListScope.getStartIndex()));
            } else {
                insertRow(getSheetId(sheetTitle), logListScope.getStartIndex(), logListScope.getStartIndex() + 1);
            }
            writeTable(createFbsPostingData(), "");
        }
    }

    public GridRange createRowRange(int rowStart, int rowEnd) {
        GridRange gridRange = new GridRange();
        gridRange.setStartRowIndex(rowStart);
        gridRange.setEndRowIndex(rowEnd);
        return gridRange;
    }

    private int getSheetId(String sheetTitle) throws IOException {
        List<Sheet> sheets = sheetsService.spreadsheets().get(spreadSheetsIds.get(FBS_LOG_LIST)).execute().getSheets();
        for (Sheet sheet : sheets) {
            if (sheet.getProperties().getTitle().equals(sheetTitle)) {
                return sheet.getProperties().getSheetId();
            }
        }
        return -1;
    }

    private boolean scopeHasPostings(SheetScope scopeToSearch) {
        for (int i = scopeToSearch.startIndex; i < scopeToSearch.endIndex; i++) {
            var row = fbsLogList.get(i);
            if (row.contains(FBS_STRING_TO_CHECK)) {
                return true;
            }
        }
        return false;
    }

    public void insertRow(int sheetId, int startIndex, int endIndex) throws IOException {
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
        sheetsService.spreadsheets().batchUpdate(spreadSheetsIds.get(FBS_LOG_LIST),
                batchUpdateRequest).execute();
    }

    public void setBackgroundColor(int sheetId, GridRange gridRange, Color color) throws IOException {
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
        sheetsService.spreadsheets().batchUpdate(spreadSheetsIds.get(FBS_LOG_LIST),
                batchUpdateRequest).execute();
    }

    private String getFbsLogListTitle() {
        //generate target sheetTitle
        String month = LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU"));
        int year = LocalDateTime.now().getYear();
        String sheetTitle = month + " " + year;

        List<Sheet> sheets;
        try {
            sheets = sheetsService.spreadsheets().get(spreadSheetsIds.get(FBS_LOG_LIST)).execute().getSheets();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Sheet sheet : sheets) {
            String tmpTitle = sheet.getProperties().getTitle();
            if (tmpTitle.equals(sheetTitle) || tmpTitle.toLowerCase().equals(sheetTitle)) {
                sheetTitle = tmpTitle;
                System.out.println("sheetTitle = " + sheetTitle);
                break;
            }
        }
        return sheetTitle;
    }

    private String getFbsLogListRange(String sheetTitle) {
        return sheetTitle + "!" + FBS_LOG_LIST_RANGE;
    }

    public void fetchFreshFbsLogListFromGoogle(String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values().get(spreadSheetsIds.get(FBS_LOG_LIST), range).execute();
        fbsLogList = response.getValues();
        normalizeFbsLogListData(fbsLogList);
    }

    /**
     * @return start and end of scope indexes
     */
    private SheetScope findLogListScope() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);
        LocalDate nextDate = LocalDate.now();
        if (nextDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextDate = nextDate.plusDays(3);
        } else {
            nextDate = nextDate.plusDays(1);
        }
        String nextDay = nextDate.format(formatter);
        int scopeStart = findScopeIndex(today);
        int scopeEnd = findScopeIndex(nextDay);
        int nonEmptyRow = findLastNonEmptyRowIndex();
        boolean isNew = false;
        if (scopeStart == -1 && scopeEnd == -1) {
            System.out.println("There is no scopeStart and no scopeEnd");
            scopeStart = nonEmptyRow + 2;
            scopeEnd = scopeStart + FBS_ROWS_TO_ADD;
            isNew = true;
        } else if (scopeEnd == -1) {
            System.out.println("There is no scopeEnd");
            scopeEnd = nonEmptyRow + FBS_ROWS_TO_ADD;
        } else {
            System.out.println("There are two scopes");
        }
        return new SheetScope(scopeStart, scopeEnd, nonEmptyRow, isNew);
    }

    private List<List<Object>> createFbsPostingData() {
        System.out.println("Create fbs posting data");

        return List.of(
                Arrays.asList("", "FBS", "нарвская", "", "", "маркетплейсы")
        );
    }

    private List<List<Object>> createScopeStartData() {
        System.out.println("Create scope start");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);

        return List.of(
                Arrays.asList(today, "", "", "", "", "")
        );
    }

    private int findScopeIndex(String targetDay) {
        int rowStart = 2;
        int index = -1;
        for (int i = rowStart; i < fbsLogList.size(); i++) {
            var row = fbsLogList.get(i);
            if (!row.isEmpty() && row.get(0).toString().equals(targetDay)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int findLastNonEmptyRowIndex() {
        int rowStart = fbsLogList.size() - 1;
        int index = -1;
        for (int i = rowStart; i > 0; i--) {
            var row = fbsLogList.get(i);
            if (!row.isEmpty() && row.stream().anyMatch(o -> o.toString().trim().length() > 5)) {
                index = i;
                System.out.println("row = " + row);
                System.out.println("index = " + index);
                break;
            }
        }
        return index;
    }

    private void normalizeFbsLogListData(List<List<Object>> fbsLogList) {
        for (List<Object> row : fbsLogList) {
            if (row.size() < 6) {
                int size = row.size();
                for (int i = 0; i < 6 - size; i++) {
                    row.add("");
                }
            }
        }
    }


    @Data
    @AllArgsConstructor
    private static class SheetScope {
        private int startIndex;
        private int endIndex;
        private int lastDataRowIndex;
        private boolean isNew;
    }

}