package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.StockItem;
import com.example.OzonHelper.domain.mapper.PostingDtoMapper;
import com.example.OzonHelper.dto.response.PostingsReportInfoResult;
import com.example.OzonHelper.dto.response.fbo.PostingDto;
import com.example.OzonHelper.dto.response.fbo.StockDto;
import com.example.OzonHelper.parser.ReportCSVParser;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.OzonHelper.util.GoogleUtils.colIndexToLetter;

@Service
public class ReportService {
    private final String REPORT_RANGE = "A1:AH1000";
    private final int SKU_START_ROW_INDEX = 2;
    private final int CLIENT_ID_COLUMN_INDEX = 0;
    private final int SKU_COLUMN_INDEX = 2;
    private final String DAILY_REPORT_SPREADSHEET_KEY = "report-table-1";

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final ReportCSVParser csvParser;
    private final PostingDtoMapper postingDtoMapper;

    public ReportService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetsProperties, GoogleClient googleClient, ReportCSVParser csvParser, PostingDtoMapper postingDtoMapper) {
        this.clients = clients;
        this.sheetsProperties = sheetsProperties;
        this.googleClient = googleClient;
        this.csvParser = csvParser;
        this.postingDtoMapper = postingDtoMapper;
    }

    public void updateReportTable() throws Exception {
        // get sku
        Map<String, List<String>> clientSkuMap = readClientIdAndSkus();

        // get stock data
        // get postings data
        // write data
        Map<String, StockItem> baseStockMap = getStringStockItemMap(clientSkuMap); // (sku, stockItem)
        System.out.println("База создана: " + baseStockMap.size() + " SKU из таблицы");

        //get and aggregate fbo stocks
        clientSkuMap.forEach((clientId, skus) -> {
            try {
                List<StockDto> stocks = clients.get(clientId).getFBOStocks(skus);
                stocks = aggregateStocks(stocks);

                for (StockDto dto : stocks) {
                    String cleanSku = dto.getSku().trim();
                    StockItem item = baseStockMap.get(cleanSku);

                    if (item != null) {
                        item.setArticle(dto.getArticle());
                        item.setAvailableStock(dto.getAvailableStock() + dto.getValidStock());
                        item.setInTransitStock(dto.getInTransitStock() + dto.getInSupplyStock());
                    }
                }
                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                System.err.println("Ошибка получения остатков для клиента " + clientId + ": " + e.getMessage());
            }
        });

        //get and aggregate postings
        Instant from = LocalDate.now().minusWeeks(3).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);

        List<String> deliverySchemas = List.of("fbo");

        Map<String, Integer> totalSellsThreeWeeksBefore = new HashMap<>();
        Map<String, Integer> totalSellsDayBefore = new HashMap<>();

        clients.forEach((s, client) -> {
            try {
                String postingsReportCode = client.createPostingsReportCode(from.toString(), to.toString(), deliverySchemas);

                PostingsReportInfoResult postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
                int attempts = 0;
                int maxAttempts = 20;

                while (!postingsReportFile.getStatus().equals("success") && attempts < maxAttempts) {
                    if ("failed".equals(postingsReportFile.getStatus())) {
                        System.err.println("Отчет для магазина " + s + " не сформирован: " + postingsReportFile.getError());
                        return; // Прерываем обработку этого магазина, но не всего цикла
                    }
                    Thread.sleep(5000);
                    postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
                    attempts++;
                }

                if (!"success".equals(postingsReportFile.getStatus())) {
                    System.err.println("Таймаут ожидания отчета для магазина " + s);
                    return;
                }
//
                List<List<String>> postings = csvParser.downloadCSV(postingsReportFile.getFile()); // raw
                if (postings == null || postings.isEmpty()) {
                    System.err.println("Пустой отчет для магазина " + s);
                    return;
                }

                postings = csvParser.filterCSV(postings, "Отменён"); // filtered

                List<PostingDto> sellsThreeWeeksBefore = new ArrayList<>();
                for (List<String> tmpPosting : postings) {
                    sellsThreeWeeksBefore.add(postingDtoMapper.mapToModel(tmpPosting));
                }

                LocalDateTime startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay();
                LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

                List<PostingDto> sellsDayBefore = sellsThreeWeeksBefore.stream()
                        .filter(postingDto -> {
                            LocalDateTime acceptDate = postingDto.getAcceptDate();
                            return !acceptDate.isBefore(startOfYesterday) && !acceptDate.isAfter(startOfToday);
                        }).toList();

                sellsThreeWeeksBefore.forEach(posting -> {
                    String sku = posting.getSku().trim(); // trim на случай пробелов
                    totalSellsThreeWeeksBefore.merge(sku, posting.getSells(), Integer::sum);
                });

                sellsDayBefore.forEach(posting -> {
                    String sku = posting.getSku().trim();
                    totalSellsDayBefore.merge(sku, posting.getSells(), Integer::sum);
                });

            } catch (IOException | CsvException | InterruptedException e) {
                System.err.println("Ошибка обработки магазина " + s + ": " + e.getMessage());
            }
        });

        // 4. "ДОЛИВАЕМ" ПРОДАЖИ В БАЗОВЫЙ СПИСОК
        totalSellsDayBefore.forEach((sku, sells) -> {
            String cleanSku = sku.trim();
            StockItem item = baseStockMap.get(cleanSku);
            if (item != null) {
                item.setSellsDayBefore(sells);
            } else {
                // Это значит, что товар был в продажах, но его нет в таблице.
                // Можно логировать, но не ломать процесс.
                System.out.println("SKU " + cleanSku + " был в продажах, но отсутствует в таблице.");
            }
        });

        totalSellsThreeWeeksBefore.forEach((sku, sells) -> {
            String cleanSku = sku.trim();
            StockItem item = baseStockMap.get(cleanSku);
            if (item != null) {
                item.setSellsThreeWeeksBefore(sells);
            }
        });

        // 5. Превращаем карту обратно в список (порядок сохранится благодаря LinkedHashMap)
        List<StockItem> resultList = new ArrayList<>(baseStockMap.values());

        System.out.println("Итоговый список для записи: " + resultList.size());

        resultList.forEach(System.out::println);

        String rangeForToday = getRangeForToday();
        List<List<Object>> date = new ArrayList<>();
        date.add(List.of(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        String spreadSheetId = sheetsProperties.getSheets().get(DAILY_REPORT_SPREADSHEET_KEY);
        String sheetName = "Лист1";

        googleClient.writeTable(date, spreadSheetId, rangeForToday);  // write date

        googleClient.writeStockItemsByDay(spreadSheetId, sheetName, resultList); // write data

    }

    private Map<String, StockItem> getStringStockItemMap(Map<String, List<String>> clientSkuMap) {
        Map<String, StockItem> baseStockMap = new LinkedHashMap<>();

        for (List<String> skus : clientSkuMap.values()) {
            for (String rawSku : skus) {
                String cleanSku = rawSku.trim();
                if (baseStockMap.containsKey(cleanSku)) continue; // защита от дублей

                StockItem item = new StockItem();
                item.setSku(cleanSku);
                item.setAvailableStock(0);
                item.setInTransitStock(0);
                item.setSellsDayBefore(0);
                item.setSellsThreeWeeksBefore(0);
                // Заполни остальные поля дефолтными значениями, если нужно

                baseStockMap.put(cleanSku, item);
            }
        }
        return baseStockMap;
    }

    public Map<String, List<String>> readClientIdAndSkus() throws IOException {
        String spreadSheetId = sheetsProperties.getSheets().get(DAILY_REPORT_SPREADSHEET_KEY);

        List<List<Object>> rawData = googleClient.fetchFreshData(spreadSheetId, REPORT_RANGE);

        return mapClientIdsToSkus(rawData);
    }

    Map<String, List<String>> mapClientIdsToSkus(List<List<Object>> rawData) {
        Map<String, List<String>> clientSkuMap = new HashMap<>();

        for (int i = SKU_START_ROW_INDEX; i < rawData.size(); i++) {
            List<Object> row = rawData.get(i);

            if (row.size() <= SKU_COLUMN_INDEX) {
                continue;
            }

            String clientId = row.get(CLIENT_ID_COLUMN_INDEX).toString().trim();
            String sku = row.get(SKU_COLUMN_INDEX).toString().trim();

            if (clientId.isEmpty() || sku.isEmpty()) {
                continue;
            }

            clientSkuMap
                    .computeIfAbsent(clientId, ignored -> new ArrayList<>())
                    .add(sku);
        }
        return clientSkuMap;
    }

    public List<StockDto> aggregateStocks(List<StockDto> stocks) {
        return stocks.stream()
                .collect(Collectors.toMap(
                        StockDto::getArticle,
                        stock -> {
                            StockDto stockDto = new StockDto();
                            stockDto.setSku(stock.getSku());
                            stockDto.setArticle(stock.getArticle());
                            stockDto.setAvailableStock(stock.getAvailableStock());
                            stockDto.setInSupplyStock(stock.getInSupplyStock());
                            stockDto.setInTransitStock(stock.getInTransitStock());
                            stockDto.setValidStock(stock.getValidStock());
                            return stockDto;
                        },
                        (first, second) -> {
                            StockDto stockDto = new StockDto();
                            stockDto.setSku(first.getSku());
                            stockDto.setArticle(first.getArticle());
                            stockDto.setAvailableStock(first.getAvailableStock() + second.getAvailableStock());
                            stockDto.setInSupplyStock(first.getInSupplyStock() + second.getInSupplyStock());
                            stockDto.setInTransitStock(first.getInTransitStock() + second.getInTransitStock());
                            stockDto.setValidStock(first.getValidStock() + second.getValidStock());
                            return stockDto;
                        },
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }

    public String getRangeForToday() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        int dayIndex = dayOfWeek.getValue() - 1; // Monday=0, Sunday=6

        int startColIndex = 4 + dayIndex * 4; // E=4
        int endColIndex = startColIndex + 3;

        String startCol = colIndexToLetter(startColIndex);
        String endCol = colIndexToLetter(endColIndex);

        // Возвращаем диапазон для всей колонки (без номера строки)
        return startCol + ":" + endCol;
    }

}
