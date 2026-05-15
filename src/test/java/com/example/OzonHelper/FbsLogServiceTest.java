package com.example.OzonHelper;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.service.FbsLogService;
import com.example.OzonHelper.util.DataNormalizer;
import com.example.OzonHelper.util.FbsLogDataBuilder;
import com.example.OzonHelper.util.FbsLogScopeCalculator;
import com.example.OzonHelper.util.SheetAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
        this.client = Mockito.mock(GoogleClient.class);
        this.calc = new FbsLogScopeCalculator(analyzer);
        this.fbsLogService = new FbsLogService(properties, client, calc, dataBuilder, normalizer);
    }


    @Test
    public void getLogListRangeTest() throws IOException {
        doNothing().when(client).writeTable(anyList(), anyString(), anyString());

        client.writeTable(List.of(List.of(123)), "123", " 123");
    }
}
