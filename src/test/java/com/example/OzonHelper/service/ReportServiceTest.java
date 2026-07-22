package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.mapper.PostingDtoMapper;
import com.example.OzonHelper.dto.response.PostingsReportInfoResult;
import com.example.OzonHelper.dto.response.fbo.StockDto;
import com.example.OzonHelper.parser.ReportCSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        clients = Map.of(
                "client-1", mock(OzonClient.class),
                "client-2", mock(OzonClient.class)
        );
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
    public void updateReportTable() throws Exception {
        //set up
//        Map<String, List<String>> clientSkuMap = Map.of(
//                "client-1", List.of("1_1", "1_2", "1_3"),
//                "client-2", List.of("2_1", "2_2")
//        );

        List<List<Object>> rawData = List.of(
                List.of("", "", ""),
                List.of("", "", ""),
                List.of("", "", ""),
                List.of("client-1", "", "1_1"),
                List.of("client-1", "", "1_2"),
                List.of("client-1", "", "1_3"),
                List.of("client-2", "", "2_1"),
                List.of("client-2", "", "2_2")
        );

        when(googleClient.fetchFreshData(any(), anyString())).thenReturn(rawData);

        List<StockDto> stocksForClientOne = generateStocksForClient("client-1", 3);
        List<StockDto> stocksForClientTwo = generateStocksForClient("client-2", 2);

        when(clients.get("client-1").getFBOStocks(anyList())).thenReturn(stocksForClientOne);
        when(clients.get("client-2").getFBOStocks(anyList())).thenReturn(stocksForClientTwo);

        String codeForClientOne = "code-1";
        String codeForClientTwo = "code-1";

        when(clients.get("client-1").createPostingsReportCode(anyString(), anyString(), anyList())).thenReturn(codeForClientOne);
        when(clients.get("client-2").createPostingsReportCode(anyString(), anyString(), anyList())).thenReturn(codeForClientTwo);

        PostingsReportInfoResult linkForClientOne = new PostingsReportInfoResult();
        linkForClientOne.setFile("https://test/client-1.csv");
        linkForClientOne.setStatus("success");
        PostingsReportInfoResult linkForClientTwo = new PostingsReportInfoResult();
        linkForClientTwo.setFile("https://test/client-2.csv");
        linkForClientTwo.setStatus("success");

        when(clients.get("client-1").getPostingsReportInfoByCode(codeForClientOne)).thenReturn(linkForClientOne);
        when(clients.get("client-2").getPostingsReportInfoByCode(codeForClientTwo)).thenReturn(linkForClientTwo);

        List<List<String>> postingsForClientOne = generatePostingsForClient("client-1", 3);
        List<List<String>> postingsForClientTwo = generatePostingsForClient("client-2", 2);

        when(csvParser.downloadCSV(linkForClientOne.getFile())).thenReturn(postingsForClientOne);
        when(csvParser.downloadCSV(linkForClientTwo.getFile())).thenReturn(postingsForClientTwo);

        //execute

        reportService.updateReportTable();
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

    // check what if new sku in postings/stocks but there are no sku in start table

    private List<StockDto> generateStocksForClient(String clientId, int amount) {
        Random rnd = new Random();
        List<StockDto> stocks = new ArrayList<>();
        String clientPostfix = clientId.substring(clientId.length() - 1);

        for (int i = 1; i <= amount; i++) {
            StockDto stock = new StockDto();

            stock.setSku(clientPostfix + "_" + i);
            stock.setArticle(i + "_" + clientPostfix);
            stock.setAvailableStock(rnd.nextInt(0, 100));
            stock.setInSupplyStock(rnd.nextInt(0, 100));
            stock.setInTransitStock(rnd.nextInt(0, 100));
            stock.setValidStock(rnd.nextInt(0, 100));

            stocks.add(stock);
        }
        return stocks;
    }

    private List<List<String>> generatePostingsForClient(String clientId, int amount) {
        Random rnd = new Random();
        List<List<String>> postings = new ArrayList<>();
        String clientPostfix = clientId.substring(clientId.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 1; i <= amount; i++) {
            List<String> posting = new ArrayList<>(Collections.nCopies(20, null));

            posting.set(10, clientPostfix + "_" + i); // set sku
            posting.set(18, String.valueOf(rnd.nextInt(0, 50))); //set sells
            posting.set(11, i + "_" + clientPostfix); // set article
            posting.set(2, LocalDateTime.now().minusDays(rnd.nextInt(1, 30)).format(formatter)); // set accept date

            postings.add(posting);
        }

        return postings;
    }

}
