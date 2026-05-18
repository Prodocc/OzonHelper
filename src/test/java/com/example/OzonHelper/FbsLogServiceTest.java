package com.example.OzonHelper;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.service.FbsLogService;
import com.example.OzonHelper.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FbsLogServiceTest {

    private FbsLogService fbsLogService;
    private GoogleSheetsProperties properties;
    private GoogleClient client;
    private FbsLogScopeCalculator calc;
    private SheetAnalyzer analyzer;
    private FbsLogDataBuilder dataBuilder;
    private DataNormalizer normalizer;

    @BeforeEach
    public void init() {
        this.properties = new GoogleSheetsProperties();
        this.dataBuilder = new FbsLogDataBuilder();
        this.normalizer = new DataNormalizer();
        this.analyzer = new SheetAnalyzer();
        this.client = mock(GoogleClient.class);
        this.calc = new FbsLogScopeCalculator(analyzer);
        this.fbsLogService = new FbsLogService(properties, client, calc, dataBuilder, normalizer);
    }


    @Test
    public void syncLogList_ShouldDoNothing_WhenThereIsOldScopeAndHasPostings() throws IOException {
        List<List<Object>> table = SheetTestDataFactory.createTable(10, 10);

        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(0).set(0, startAndEndDateForTest[0]); // set startScope
        table.get(table.size() / 2).set(0, startAndEndDateForTest[1]); // set endScope

        table.set(1, dataBuilder.createFbsPostingData().get(0));

        when(client.fetchFreshData(anyString(), anyString())).thenReturn(table);
        when(client.getSheetIdByTitle(anyString(), anyString())).thenReturn(123);

        fbsLogService.syncLogList();

        verify(client, never()).writeTable(anyList(), anyString(), anyString());
    }

    @Test
    public void syncLogList_ShouldExpandScopeAndWritePostings_WhenThereIsOldScopeAndNoPostings() throws IOException {
        List<List<Object>> table = SheetTestDataFactory.createTable(10, 10);

        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(0).set(0, startAndEndDateForTest[0]); // set startScope
        table.get(table.size() / 2).set(0, startAndEndDateForTest[1]); // set endScope

        when(client.fetchFreshData(anyString(), anyString())).thenReturn(table);
        when(client.getSheetIdByTitle(anyString(), anyString())).thenReturn(123);

        fbsLogService.syncLogList();

        verify(client, times(1)).insertRow(anyString(), anyInt(), anyInt(), anyInt());
        verify(client, times(1)).writeTable(anyList(), anyString(), anyString());
    }

    @Test
    public void syncLogList_ShouldWritePostings_WhenThereIsNewScope() throws IOException {

    }
}
