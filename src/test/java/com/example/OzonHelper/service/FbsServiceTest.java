package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.util.*;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.GridRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FbsServiceTest {

    private FbsService fbsService;
    private Map<String, OzonClient> clients;
    private GoogleSheetsProperties properties;
    private GoogleClient googleClient;
    private FbsLogScopeCalculator calc;
    private SheetAnalyzer analyzer;
    private FbsLogDataBuilder dataBuilder;
    private DataNormalizer normalizer;

    @BeforeEach
    public void init() {
        this.clients = mock(Map.class);
        this.properties = new GoogleSheetsProperties();
        properties.getSheets().put("fbs-log-id", "1eI8apB8e7PCzJAQ8HRcJrUWX1Zfffk7UwzxoIbSjDp4");
        this.dataBuilder = new FbsLogDataBuilder();
        this.normalizer = new DataNormalizer();
        this.analyzer = new SheetAnalyzer();
        this.googleClient = mock(GoogleClient.class);
        this.calc = new FbsLogScopeCalculator(analyzer);
        this.fbsService = new FbsService(clients, properties, googleClient, calc, dataBuilder, normalizer);
    }


    @Test
    public void syncLogList_ShouldDoNothing_WhenThereIsOldScopeAndHasPostings() throws IOException {
        List<List<Object>> table = SheetTestDataFactory.createTable(10, 10);

        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(0).set(0, startAndEndDateForTest[0]); // set startScope
        table.get(table.size() / 2).set(0, startAndEndDateForTest[1]); // set endScope

        table.set(1, dataBuilder.createFbsPostingData().get(0));

        when(googleClient.readTable(anyString(), anyString())).thenReturn(table);

        fbsService.syncLogList();

        verify(googleClient, never()).writeTable(anyList(), anyString(), anyString());
    }

    @Test
    public void syncLogList_ShouldExpandScopeAndWritePostings_WhenThereIsOldScopeAndNoPostings() throws IOException {
        List<List<Object>> table = SheetTestDataFactory.createTable(10, 10);

        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(0).set(0, startAndEndDateForTest[0]); // set startScope
        table.get(table.size() / 2).set(0, startAndEndDateForTest[1]); // set endScope

        when(googleClient.readTable(anyString(), anyString())).thenReturn(table);

        fbsService.syncLogList();

        verify(googleClient, times(1)).insertRow(anyString(), anyInt(), anyInt(), anyInt());
        verify(googleClient, times(1)).writeTable(anyList(), anyString(), anyString());
    }

    @Test
    public void syncLogList_ShouldWritePostings_WhenThereIsNewScope() throws IOException {
        List<List<Object>> table = SheetTestDataFactory.createTable(10, 10);

        when(googleClient.readTable(anyString(), anyString())).thenReturn(table);

        fbsService.syncLogList();

        verify(googleClient, times(2)).writeTable(anyList(), anyString(), anyString());
        verify(googleClient, times(1)).setBackgroundColor(anyString(), anyInt(), any(GridRange.class), any(Color.class));

    }
}
