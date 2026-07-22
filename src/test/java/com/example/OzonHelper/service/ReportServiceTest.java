package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.StockItem;
import com.example.OzonHelper.domain.mapper.PostingDtoMapper;
import com.example.OzonHelper.dto.response.fbo.StockDto;
import com.example.OzonHelper.parser.ReportCSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ReportServiceTest {
    private ReportService reportService;
    private Map<String, OzonClient> clients;
    private GoogleClient googleClient;
    private GoogleSheetsProperties properties;
    private ReportCSVParser csvParser;
    private PostingDtoMapper dtoMapper;

    @BeforeEach
    public void init() {
        clients = Map.of();
        googleClient = mock(GoogleClient.class);
        properties = mock(GoogleSheetsProperties.class);
        csvParser = mock(ReportCSVParser.class);
        dtoMapper = mock(PostingDtoMapper.class);
        this.reportService = new ReportService(
                clients,
                properties,
                googleClient,
                csvParser,
                dtoMapper);
    }

    @Test
    public void updateReportTable() throws IOException, InterruptedException {
        //set up
//        Map<String, List<String>> clientSkuMap = Map.of(
//                "client-1", List.of("1_1", "1_2", "1_3"),
//                "client-2", List.of("2_1", "2_2")
//        );

        List<List<Object>> rawData = List.of(
                List.of("client-1", "", "1_1"),
                List.of("client-1", "", "1_2"),
                List.of("client-1", "", "1_3"),
                List.of("client-2", "", "2_1"),
                List.of("client-2", "", "2_2")
        );
        List<StockDto> stocksForClientOne = generateStocksForClient("client-1", 3);
        List<StockDto> stocksForClientTwo = generateStocksForClient("client-2", 2);

        System.out.println(stocksForClientOne);
        System.out.println(stocksForClientTwo);

        when(googleClient.fetchFreshData(anyString(), anyString())).thenReturn(rawData);
        when(clients.get(eq("client-1")).getFBOStocks(anyList())).thenReturn(stocksForClientOne);
        when(clients.get(eq("client-2")).getFBOStocks(anyList())).thenReturn(stocksForClientTwo);
        //execute
        //assert
    }


    @Test
    public void shouldMapClientIdsToSkusIgnoringEmptyValues() {
        List<List<Object>> rawData = List.of(
                List.of("", "", ""),
                List.of("", "", ""),
                List.of("", ""),
                List.of("client-1", "", "sku-1"),
                List.of("client-1", "", "sku-2"),
                List.of("client-2", "", "sku-3"),
                List.of("", "", "sku-4"),
                List.of("client-3", "", ""),
                List.of("client-4", "", "sku-5"),
                List.of("client-4", "", "sku-6")
        );

        Map<String, List<String>> result = reportService.mapClientIdsToSkus(rawData);


        Map<String, List<String>> map = Map.of(
                "client-1", List.of("sku-1", "sku-2"),
                "client-2", List.of("sku-3"),
                "client-4", List.of("sku-5", "sku-6")
        );
        assertThat(result).isEqualTo(map);
    }

    private List<StockDto> generateStocksForClient(String clientId, int amount) {
        Random rnd = new Random();
        List<StockDto> stocks = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            StockDto stock = new StockDto();

            String clientPostfix = clientId.substring(clientId.length() - 1);

            stock.setSku(clientPostfix + "_" + i);
            stock.setAvailableStock(rnd.nextInt(0, 100));
            stock.setInSupplyStock(rnd.nextInt(0, 100));
            stock.setInTransitStock(rnd.nextInt(0, 100));
            stock.setValidStock(rnd.nextInt(0, 100));

            stocks.add(stock);
        }
        return stocks;
    }

}
