package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.enums.SheetColors;
import com.example.OzonHelper.util.*;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Sheet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class FbsLogService {
    private final String LOG_LIST_RANGE = "B1:J500";
    private final String FBS_LOG_LIST_SPREADSHEET_KEY = "fbs-log-id";
    private final String FBS_LOG_LIST_DATE_COLUMN = "B";
    private final String FBS_LOG_LIST_PARTNER_COLUMN = "C";
    private final String FBS_LOG_LIST_ADDRESS_COLUMN = "D";
    private final String FBS_LOG_LIST_MANAGER_COLUMN = "G";
    private final String FBS_STRING_TO_CHECK = "FBS";
    private final int FBS_LOG_LIST_ROW_SIZE = 6;
    private final int FBS_ROWS_TO_ADD = 5;

    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final FbsLogScopeCalculator scopeCalculator;
    private final FbsLogDataBuilder dataBuilder;
    private final DataNormalizer normalizer;

    public FbsLogService(GoogleSheetsProperties sheetsProperties, GoogleClient googleClient, FbsLogScopeCalculator scopeCalculator,
                         FbsLogDataBuilder dataBuilder, DataNormalizer normalizer) {
        this.sheetsProperties = sheetsProperties;
        this.googleClient = googleClient;
        this.scopeCalculator = scopeCalculator;
        this.dataBuilder = dataBuilder;
        this.normalizer = normalizer;
    }

    // calls only if there are fbs postings
    public void syncLogList() throws IOException {
        String spreadSheetId = sheetsProperties.getSheets().get(FBS_LOG_LIST_SPREADSHEET_KEY);

        String title = getLogListTitle(spreadSheetId);
        String range = getLogListRange(title, LOG_LIST_RANGE);

        List<List<Object>> rawData = googleClient.fetchFreshData(spreadSheetId, range);

        normalizer.normalizeData(rawData, FBS_LOG_LIST_ROW_SIZE);

        SheetScope sheetScope = scopeCalculator.calculateScope(rawData, FBS_ROWS_TO_ADD);

        int sheetId = googleClient.getSheetIdByTitle(title, spreadSheetId);

        if (!sheetScope.isNew() && scopeCalculator.hasPostings(sheetScope, rawData, FBS_STRING_TO_CHECK)) {
            System.out.println("A");
            return;
        }
        if (sheetScope.isNew()) {
            System.out.println("B");
            writeNewScope(spreadSheetId, sheetId, title, sheetScope);
        } else {
            System.out.println("C");
            expandExistingScope(spreadSheetId, title, sheetScope);
        }

        String postingRange = GoogleUtils.buildRange(
                title,
                FBS_LOG_LIST_DATE_COLUMN,
                FBS_LOG_LIST_MANAGER_COLUMN,
                sheetScope.getStartIndex() + 1);
        googleClient.writeTable(dataBuilder.createFbsPostingData(), spreadSheetId, postingRange);
    }

    public String getLogListRange(String sheetTitle, String range) {
        return sheetTitle + "!" + range;
    }

    public void writeNewScope(String spreadSheetId, int sheetId, String sheetTitle, SheetScope scope) throws IOException {
        List<List<Object>> scopeStartData = dataBuilder.createScopeStartData();
        String writeRange = GoogleUtils.buildRange(
                sheetTitle,
                FBS_LOG_LIST_DATE_COLUMN,
                FBS_LOG_LIST_MANAGER_COLUMN,
                scope.getStartIndex());

        googleClient.writeTable(scopeStartData, spreadSheetId, writeRange);

        GridRange colorRange = GoogleUtils.createRowRange(scope.getStartIndex(), scope.getStartIndex() + 1);
        googleClient.setBackgroundColor(spreadSheetId, sheetId, colorRange, SheetColors.FBS_LIST_COLOR_DATE_COLOR.toGoogleColor());
    }

    public void expandExistingScope(String spreadSheetId, String sheetTitle, SheetScope scope) throws IOException {
        googleClient.insertRow(spreadSheetId, googleClient.getSheetIdByTitle(sheetTitle, spreadSheetId), scope.getStartIndex(), scope.getStartIndex() + 1);
    }

    //TODO: use adapter for google client sheet

    public String getLogListTitle(String spreadSheetId) throws IOException {
        System.out.println("spreadSheetId = " + spreadSheetId);
        String month = LocalDateTime.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU"));
        int year = LocalDateTime.now().getYear();
        String sheetTitle = month + " " + year;

        List<Sheet> sheets;
        sheets = googleClient.getSheets(spreadSheetId);
        for (Sheet sheet : sheets) {
            String tmpTitle = sheet.getProperties().getTitle();
            if (tmpTitle.equals(sheetTitle) || tmpTitle.toLowerCase().equals(sheetTitle)) {
                sheetTitle = tmpTitle;
                break;
            }
        }
        return sheetTitle;
    }

}
