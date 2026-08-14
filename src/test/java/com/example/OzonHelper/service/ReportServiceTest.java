package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.StockItem;
import com.example.OzonHelper.domain.mapper.PostingAccrualMapper;
import com.example.OzonHelper.domain.mapper.PostingDtoMapper;
import com.example.OzonHelper.domain.mapper.SupplyOrderCompositionMapper;
import com.example.OzonHelper.dto.response.PostingsReportInfoResult;
import com.example.OzonHelper.dto.response.fbo.*;
import com.example.OzonHelper.enums.ClusterType;
import com.example.OzonHelper.enums.SupplyState;
import com.example.OzonHelper.parser.ReportCSVParser;
import com.example.OzonHelper.parser.ReportExcelParser;
import com.example.OzonHelper.util.SheetAnalyzer;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.Mockito.*;

public class ReportServiceTest {
    private ReportService reportService;
    private Map<String, OzonClient> clients;
    private GoogleClient googleClient;
    private SheetAnalyzer sheetAnalyzer;
    private GoogleSheetsProperties properties;
    private ReportCSVParser csvParser;
    private ReportExcelParser excelParser;
    private PostingDtoMapper dtoMapper;
    private PostingAccrualMapper accrualMapper;
    private SupplyOrderCompositionMapper compositionMapper;

    @BeforeEach
    public void init() {
        clients = Map.of(
                "client-1", mock(OzonClient.class),
                "client-2", mock(OzonClient.class)
        );
        googleClient = mock(GoogleClient.class);
        sheetAnalyzer = mock(SheetAnalyzer.class);
        properties = mock(GoogleSheetsProperties.class);
        csvParser = mock(ReportCSVParser.class);
        excelParser = mock(ReportExcelParser.class);
        dtoMapper = new PostingDtoMapper();
        accrualMapper = new PostingAccrualMapper();
        compositionMapper = new SupplyOrderCompositionMapper();
        this.reportService = new ReportService(
                clients,
                properties,
                googleClient,
                sheetAnalyzer,
                csvParser,
                excelParser,
                dtoMapper,
                accrualMapper,
                compositionMapper
        );
    }

    @Test
    public void processCrossdockReport() throws CsvValidationException, IOException, InterruptedException {

        OzonClient ozonClient = clients.get("client-1");

        Path fullPath = Path.of("my_path\\reports\\crossdock\\incoming\\report_01.08.2026-13.08.2025.xlsx");

        List<List<String>> excelList = List.of(
                List.of("101", "creationDate", "groupOfService", "Кросс-докинг", "article1", "sku1", "productName", "0", "sellerPrice", "orderProcessType", "platform", "Schema", "", "", "", "-659,81 ₽"),
                List.of("102", "creationDate", "groupOfService", "Кросс-докинг", "article2", "sku2", "productName", "0", "sellerPrice", "orderProcessType", "platform", "Schema", "", "", "", "-812,00 ₽"),
                List.of("103", "creationDate", "groupOfService", "Кросс-докинг", "article3", "sku3", "productName", "0", "sellerPrice", "orderProcessType", "platform", "Schema", "", "", "", "0,00 ₽"),
                List.of("103", "creationDate", "groupOfService", "Эквайринг", "article3", "sku3", "productName", "0", "sellerPrice", "orderProcessType", "platform", "Schema", "", "", "", "0,00 ₽"),
                List.of("104", "creationDate", "groupOfService", "Кросс-докинг", "article4", "sku4", "productName", "0", "sellerPrice", "orderProcessType", "platform", "Schema", "", "", "", "-400,00 ₽"),
                List.of("104", "creationDate", "groupOfService", "Кросс-докинг", "article5", "sku5", "productName", "0", "sellerPrice", "orderProcessType", "platform", "Schema", "", "", "", "-400,00 ₽"));

        when(excelParser.readCSV(any(Path.class))).thenReturn(excelList);

        when(ozonClient.getShopName()).thenReturn("shopName");

        List<String> supplyOrderIds = List.of("101", "102", "103", "104", "105", "106");

        SupplyOrdersPage page = new SupplyOrdersPage(supplyOrderIds, "1");
        when(ozonClient.getSupplyOrdersIds(any(), any(SupplyState.class))).thenReturn(page);

        //create supplies, set supplies
        List<SupplyOrderDto> supplyDtos = new ArrayList<>();

        // first supplyOrder
        SupplyOrderDto dto1 = new SupplyOrderDto();
        dto1.setCreationDate(LocalDateTime.now());
        dto1.setOrderNumber("101");

        List<SupplyInfoDto> supplies1 = new ArrayList<>();
        SupplyInfoDto supplyInfoDto1 = new SupplyInfoDto();
        supplyInfoDto1.setSupplyId("101");
        supplyInfoDto1.setBundleId("101");
        supplyInfoDto1.setClusterId(0);

        SupplyInfoDto supplyInfoDto2 = new SupplyInfoDto();
        supplyInfoDto2.setSupplyId("102");
        supplyInfoDto2.setBundleId("102");
        supplyInfoDto2.setClusterId(1);

        supplies1.add(supplyInfoDto1);
        supplies1.add(supplyInfoDto2);

        dto1.setSupplies(supplies1);

        // second
        SupplyOrderDto dto2 = new SupplyOrderDto();
        dto2.setCreationDate(LocalDateTime.now());
        dto2.setOrderNumber("102");

        List<SupplyInfoDto> supplies2 = new ArrayList<>();
        SupplyInfoDto supplyInfoDto3 = new SupplyInfoDto();
        supplyInfoDto3.setSupplyId("103");
        supplyInfoDto3.setBundleId("103");
        supplyInfoDto3.setClusterId(2);

        supplies2.add(supplyInfoDto3);

        dto2.setSupplies(supplies2);

        //third
        SupplyOrderDto dto3 = new SupplyOrderDto();
        dto3.setCreationDate(LocalDateTime.now());
        dto3.setOrderNumber("104");

        List<SupplyInfoDto> supplies3 = new ArrayList<>();
        SupplyInfoDto supplyInfoDto4 = new SupplyInfoDto();
        supplyInfoDto4.setSupplyId("104");
        supplyInfoDto4.setBundleId("104");
        supplyInfoDto4.setClusterId(3);

        supplies3.add(supplyInfoDto4);

        dto3.setSupplies(supplies3);

        supplyDtos.add(dto1);
        supplyDtos.add(dto2);
        supplyDtos.add(dto3);

        when(ozonClient.getSupplyOrders(supplyOrderIds)).thenReturn(supplyDtos);

        List<ClusterDto> clusters = getClusters();
        when(ozonClient.getClusters(any(ClusterType.class))).thenReturn(clusters);

        //create orderCompositionDtos
        List<ItemDto> items1 = new ArrayList<>();
        ItemDto item1 = new ItemDto();
        item1.setSku("sku1");
        item1.setArticle("article1");
        item1.setQuantity(101);

        items1.add(item1);

        SupplyOrderCompositionDto composition1 = new SupplyOrderCompositionDto(items1, 1);

        List<ItemDto> items2 = new ArrayList<>();
        ItemDto item2 = new ItemDto();
        item2.setSku("sku2");
        item2.setArticle("article2");
        item2.setQuantity(102);

        items2.add(item2);

        SupplyOrderCompositionDto composition2 = new SupplyOrderCompositionDto(items2, 1);

        List<ItemDto> items3 = new ArrayList<>();
        ItemDto item3 = new ItemDto();
        item3.setSku("sku3");
        item3.setArticle("article3");
        item3.setQuantity(103);

        items3.add(item3);

        SupplyOrderCompositionDto composition3 = new SupplyOrderCompositionDto(items3, 1);

        List<ItemDto> items4 = new ArrayList<>();
        ItemDto item4 = new ItemDto();
        item4.setSku("sku4");
        item4.setArticle("article4");
        item4.setQuantity(104);

        ItemDto item5 = new ItemDto();
        item5.setSku("sku5");
        item5.setArticle("article5");
        item5.setQuantity(105);

        items4.add(item4);
        items4.add(item5);

        SupplyOrderCompositionDto composition4 = new SupplyOrderCompositionDto(items4, 2);

        when(ozonClient.getSupplyOrdersComposition(List.of(supplyInfoDto1.getBundleId()))).thenReturn(composition1);
        when(ozonClient.getSupplyOrdersComposition(List.of(supplyInfoDto2.getBundleId()))).thenReturn(composition2);
        when(ozonClient.getSupplyOrdersComposition(List.of(supplyInfoDto3.getBundleId()))).thenReturn(composition3);
        when(ozonClient.getSupplyOrdersComposition(List.of(supplyInfoDto4.getBundleId()))).thenReturn(composition4);

        when(properties.getSheets()).thenReturn(Map.of("crossdock-report-table", "spreeadSheetId-1"));

        // no new sheet creating
        when(googleClient.hasSheet(anyString(), anyString())).thenReturn(1);

        // ignore formatting
        doNothing().when(googleClient).formatCrossDockSheet(anyString(), anyInt());

        when(ozonClient.getShopName()).thenReturn("shop1");

        when(googleClient.readTable(anyString(), anyString())).thenReturn(List.of());
        doNothing().when(googleClient).writeTable(anyList(), anyString(), anyString());

        List<List<Object>> rawData = List.of(
                List.of("shop1", "101", "Москва", "sku1", "article1", 101, new BigDecimal("-659.81"), new BigDecimal("-6.54")),
                List.of("shop1", "102", "СПБ", "sku2", "article2", 102, new BigDecimal("-812.00"), new BigDecimal("-7.97")),
                List.of("shop1", "103", "Ростов", "sku3", "article3", 103, new BigDecimal("0.00"), new BigDecimal("0.00")),
                List.of("shop1", "104", "Новосибирск", "sku4", "article4", 104, new BigDecimal("-800.00"), new BigDecimal("-3.83")),
                List.of("shop1", "104", "Новосибирск", "sku5", "article5", 105, new BigDecimal("-800.00"), new BigDecimal("-3.83"))
        );

        ArgumentCaptor<List<List<Object>>> captor = ArgumentCaptor.forClass(List.class);

        // execute
        reportService.processCrossdockReport("client-1", fullPath);

        verify(googleClient).writeTable(captor.capture(), anyString(), anyString());

        List<List<Object>> value = captor.getValue();
        assertThat(value).isEqualTo(rawData);

    }

    private List<ClusterDto> getClusters() {
        String[] clusterNames = new String[]{"Москва", "СПБ", "Ростов", "Новосибирск"};
        List<ClusterDto> result = new ArrayList<>();
        for (int i = 0; i < clusterNames.length; i++) {
            ClusterDto dto = new ClusterDto();
            dto.setMacrolocalClusterId(i);
            dto.setName(clusterNames[i]);
            result.add(dto);
        }
        return result;
    }

    @Test
    public void updateDailyReport() throws Exception {
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

        when(googleClient.readTable(any(), anyString())).thenReturn(rawData);

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

        when(csvParser.readCSVFromUrl(linkForClientOne.getFile())).thenReturn(postingsForClientOne);
        when(csvParser.readCSVFromUrl(linkForClientTwo.getFile())).thenReturn(postingsForClientTwo);
        when(csvParser.filterCSV(postingsForClientOne, "Отменён")).thenReturn(postingsForClientOne);
        when(csvParser.filterCSV(postingsForClientTwo, "Отменён")).thenReturn(postingsForClientTwo);
        doNothing().when(googleClient).writeTable(anyList(), anyString(), anyString());
        doNothing().when(googleClient).writeDailyReportItems(anyString(), anyString(), anyList());

        ArgumentCaptor<List<StockItem>> captor = ArgumentCaptor.forClass(List.class);

        //execute
        reportService.updateDailyReport(false);
        //assert

        verify(googleClient).writeDailyReportItems(
                any(),
                anyString(),
                captor.capture()
        );

        List<StockItem> actual = captor.getValue();
        assertThat(actual).
                extracting(
                        StockItem::getSku,
                        StockItem::getArticle,
                        StockItem::getAvailableStock,
                        StockItem::getInTransitStock,
                        StockItem::getSellsForYesterday,
                        StockItem::getSellsForLastThreeWeeks
                )
                .containsExactlyInAnyOrder(
                        tuple("1_1", "1_1", 2, 2, 1, 1),
                        tuple("1_2", "2_1", 4, 4, 0, 2),
                        tuple("1_3", "3_1", 6, 6, 0, 3),
                        tuple("2_1", "1_2", 2, 2, 1, 1),
                        tuple("2_2", "2_2", 4, 4, 0, 2)
                );
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
        List<StockDto> stocks = new ArrayList<>();
        String clientPostfix = clientId.substring(clientId.length() - 1);

        for (int i = 1; i <= amount; i++) {
            StockDto stock = new StockDto();

            stock.setSku(clientPostfix + "_" + i);
            stock.setArticle(i + "_" + clientPostfix);
            stock.setAvailableStock(i);
            stock.setInSupplyStock(i);
            stock.setInTransitStock(i);
            stock.setValidStock(i);

            stocks.add(stock);
        }
        return stocks;
    }

    private List<List<String>> generatePostingsForClient(String clientId, int amount) {
        List<List<String>> postings = new ArrayList<>();
        String clientPostfix = clientId.substring(clientId.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 1; i <= amount; i++) {
            List<String> posting = new ArrayList<>(Collections.nCopies(20, null));

            posting.set(10, clientPostfix + "_" + i); // set sku
            posting.set(18, String.valueOf(i)); //set sells
            posting.set(11, i + "_" + clientPostfix); // set article
            posting.set(2, LocalDateTime.now().minusDays(i).format(formatter)); // set accept date

            postings.add(posting);
        }

        return postings;
    }


}
