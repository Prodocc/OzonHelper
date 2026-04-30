package com.example.OzonHelper.util;

import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class TableManager {
    private static final String FBS_LOG_LIST = "fbs-log-id";
    private static final String FBS_LOG_LIST_RANGE = "B1:J500";
    private final String FBS_LOG_LIST_DATE_COLUMN = "B";
    private final String FBS_LOG_LIST_PARTNER_COLUMN = "C";
    private final String FBS_LOG_LIST_ADDRESS_COLUMN = "D";
    private final String FBS_LOG_LIST_MANAGER_COLUMN = "G";

    private final Map<String, String> sheetsIds;
    private final Sheets sheetsService;

    public TableManager(GoogleSheetsProperties properties, Sheets sheetsService) {
        this.sheetsIds = properties.getSheets();
        this.sheetsService = sheetsService;
    }

    public void writeFbsPostings() {
        //check for postings
        //if yes then return
        //else write
    }

    public void CheckAndWriteFbsLogListPostings() throws IOException {
        String range = getFbsLogListRange();
        int logListScopeStart = findLogListScope(range);
        if (logListScopeStart >= 0) {
        }
        writeFbsPostings();
        //find our scope, if there is, then look for record, if there isn't look for blank row or add new row
        //if there isn't, then add new scope(scope start only)
        //if the day isn't the last in the month, else create new list, create header, add new scope(or add all scope for all month)
        //check for postings in scope
    }

    private boolean checkScopePostings() {
        return false;
    }

    private String getFbsLogListRange() {
        //generate target sheetTitle
        String month = LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU"));
        int year = LocalDateTime.now().getYear();
        String sheetTitle = month + " " + year;

        List<Sheet> sheets;
        try {
            sheets = sheetsService.spreadsheets().get(sheetsIds.get(FBS_LOG_LIST)).execute().getSheets();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //find our list title
        for (Sheet sheet : sheets) {
            String tmpTitle = sheet.getProperties().getTitle();
            if (tmpTitle.equals(sheetTitle) || tmpTitle.toLowerCase().equals(sheetTitle)) {
                sheetTitle = tmpTitle;
                System.out.println("sheetTitle = " + sheetTitle);
                break;
            }
        }
        return sheetTitle + "!" + FBS_LOG_LIST_RANGE;
    }

    /**
     * @return value of the scope start, if there is no scope return -1
     */
    private int findLogListScope(String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values().get(sheetsIds.get(FBS_LOG_LIST), range).execute();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);
        int rowStart = 2;
        int scopeStart;
        List<List<Object>> values = response.getValues();
        for (int i = rowStart; i < values.size(); i++) {
            var row = values.get(i);
            if (!row.isEmpty() && row.get(0).toString().equals(today)) {
                scopeStart = i;
                return scopeStart;
            }
        }
        return -1;
    }

    private void createNewScope() {

    }

    private void getFBSLogListSheetData(String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values().get(sheetsIds.get(FBS_LOG_LIST), range).execute();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);
        int rowStart = 2;
        int scopeStart = 0;
        List<List<Object>> values = response.getValues();
        for (int i = rowStart; i < values.size(); i++) {
            var row = values.get(i);
            if (!row.isEmpty() && row.get(0).toString().equals(today)) {
                scopeStart = i;
                break;
            }
        }

        List<List<Object>> data = List.of(
                Arrays.asList("", "фбс", "нарвская", "", "", "маркетплейсы", "", "", "")
        );

        ValueRange body = new ValueRange().setValues(data);

        sheetsService.spreadsheets().values()
                .update(sheetsIds.get(FBS_LOG_LIST), "Апрель 2026!B265:J265", body)
                .setValueInputOption("RAW")
                .execute();
    }

}