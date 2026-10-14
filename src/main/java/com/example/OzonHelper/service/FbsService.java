package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.dto.response.fbs.PostingDto;
import com.example.OzonHelper.enums.ozon.PostingStatus;
import com.example.OzonHelper.enums.ozon.SheetColors;
import com.example.OzonHelper.util.*;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Sheet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class FbsService {
    private final String LOG_LIST_RANGE = "B1:J500";
    private final String FBS_LOG_LIST_SPREADSHEET_KEY = "fbs-log-id";
    private final String FBS_LOG_LIST_DATE_COLUMN = "B";
    private final String FBS_LOG_LIST_PARTNER_COLUMN = "C";
    private final String FBS_LOG_LIST_ADDRESS_COLUMN = "D";
    private final String FBS_LOG_LIST_MANAGER_COLUMN = "G";
    private final String FBS_STRING_TO_CHECK = "FBS";
    private final int FBS_LOG_LIST_ROW_SIZE = 6;
    private final int FBS_ROWS_TO_ADD = 5;

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final FbsLogScopeCalculator scopeCalculator;
    private final FbsLogDataBuilder dataBuilder;
    private final DataNormalizer normalizer;

    public FbsService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetsProperties, GoogleClient googleClient, FbsLogScopeCalculator scopeCalculator,
                      FbsLogDataBuilder dataBuilder, DataNormalizer normalizer) {
        this.clients = clients;
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

        List<List<Object>> rawData = googleClient.readTable(spreadSheetId, range);

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
        googleClient.setBackgroundColor(spreadSheetId, sheetId, colorRange, SheetColors.FBS_LIST_DATE_COLOR.toGoogleColor());
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

    //TODO create FBS orderException
    //TODO add methods that will populate LogList with postings //rewrite syncLogList maybe
    //TODO add method that will check all shop from fbs posting with status AWAITING_DELIVER, then form shipment and send to warehouse mail address
    //TODO add method that will check all shop from fbs postings with status AWAITING_PACKAGING, then for each of these postings ->
    //create order in 1C, and send it to the warehouse mail address with the posting barcode label
    public List<String> getShopsWithAwaitingPackaging() {
        List<String> result = new ArrayList<>();

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(5);

        for (OzonClient client : clients.values()) {
            try {
                List<PostingDto> postings = client.getFbsPosting(from, to, PostingStatus.AWAITING_PACKAGING);
                if (!postings.isEmpty()) result.add(client.getShopName());
            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to check FBS orders for shop: " + client.getShopName(),
                        e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(
                        "Interrupted while loading FBS postings for shop: " + client.getShopName(),
                        e
                );
            }
        }
        return result;
    }

    public List<String> getShopsWithAwaitingDeliver() {
        List<String> result = new ArrayList<>();

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(5);

        for (OzonClient client : clients.values()) {
            try {
                List<PostingDto> postings = client.getFbsPosting(from, to, PostingStatus.AWAITING_DELIVER);
                if (!postings.isEmpty()) result.add(client.getShopName());
            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to check FBS orders for shop: " + client.getShopName(),
                        e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(
                        "Interrupted while loading FBS postings for shop: " + client.getShopName(),
                        e
                );
            }
        }
        return result;
    }

}
