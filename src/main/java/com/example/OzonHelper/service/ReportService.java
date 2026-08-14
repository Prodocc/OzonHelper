package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.*;
import com.example.OzonHelper.domain.mapper.PostingAccrualMapper;
import com.example.OzonHelper.domain.mapper.PostingDtoMapper;
import com.example.OzonHelper.domain.mapper.SupplyOrderCompositionMapper;
import com.example.OzonHelper.dto.report.ozon.PostingAccrualDto;
import com.example.OzonHelper.dto.response.PostingsReportInfoResult;
import com.example.OzonHelper.dto.response.fbo.*;
import com.example.OzonHelper.enums.AccrualType;
import com.example.OzonHelper.enums.ClusterType;
import com.example.OzonHelper.enums.SupplyState;
import com.example.OzonHelper.exceptions.ReportCreatingException;
import com.example.OzonHelper.parser.ReportCSVParser;
import com.example.OzonHelper.parser.ReportExcelParser;
import com.example.OzonHelper.util.SheetAnalyzer;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.OzonHelper.util.GoogleUtils.colIndexToLetter;

@Service
public class ReportService {
    private final String REPORT_RANGE = "A1:AH1000";
    private final int SKU_START_ROW_INDEX = 2;
    private final int CLIENT_ID_COLUMN_INDEX = 0;
    private final int SKU_COLUMN_INDEX = 2;
    private final String DAILY_REPORT_SPREADSHEET_KEY = "daily-report-table";
    private final String WEEKLY_REPORT_SPREADSHEET_KEY = "weekly-report-table";
    private final String CROSSDOCK_REPORT_SPREADSHEET_KEY = "crossdock-report-table";
    private final String DAILY_REPORT_SHEET_NAME = "Лист1";
    private final String WEEKLY_REPORT_SHEET_NAME = "Продажи еженедельные";
    private final int ACCRUAL_REPORT_SUPPLY_ID_FIELD_INDEX = 0;
    private final int ACCRUAL_REPORT_TYPE_FIELD_INDEX = 3;
    private final int ACCRUAL_REPORT_CARGO_SPACE_COUNT_FIELD_INDEX = 7;
    private final int ACCRUAL_REPORT_SUM_FIELD_INDEX = 15;

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final SheetAnalyzer sheetAnalyzer;
    private final ReportCSVParser csvParser;
    private final ReportExcelParser excelParser;
    private final PostingDtoMapper postingDtoMapper;
    private final SupplyOrderCompositionMapper compositionMapper;
    private final PostingAccrualMapper postingAccrualMapper;
    private Map<Long, String> clustersById;

    public ReportService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetsProperties,
                         GoogleClient googleClient, SheetAnalyzer sheetAnalyzer,
                         ReportCSVParser csvParser, ReportExcelParser excelParser,
                         PostingDtoMapper postingDtoMapper, PostingAccrualMapper postingAccrualMapper, SupplyOrderCompositionMapper compositionMapper) {
        this.clients = clients;
        this.sheetsProperties = sheetsProperties;
        this.googleClient = googleClient;
        this.sheetAnalyzer = sheetAnalyzer;
        this.csvParser = csvParser;
        this.excelParser = excelParser;
        this.postingDtoMapper = postingDtoMapper;
        this.postingAccrualMapper = postingAccrualMapper;
        this.compositionMapper = compositionMapper;
    }

    public void processCrossdockReport(String clientId, Path fullPath) throws CsvValidationException, IOException, InterruptedException {
        OzonClient client = clients.get(clientId);
        System.out.println("clientId = " + clientId);
        System.out.println("fullPath = " + fullPath);

        List<List<String>> excelList = excelParser.readCSV(fullPath);

        List<PostingAccrualDto> accrualDtos = buildAccrualsDtos(excelList);

        List<PostingAccrualDto> crossDockAccrualsDtos = accrualDtos
                .stream()
                .filter(postingAccrualDto -> postingAccrualDto.getType().equals(AccrualType.CROSSDOCK.getDescription())).toList();

        List<PostingAccrual> crossDockAccruals = buildCrossDockAccruals(crossDockAccrualsDtos, client.getShopName(), fullPath);

        Map<String, PostingAccrual> accrualsBySupplyId = aggregateAccrualsBySupplyId(crossDockAccruals);

        List<String> supplyOrderIds = getAllSupplyOrderIds(client);

        List<SupplyOrderDto> supplyOrderDtos = getSupplyOrderDtos(client, supplyOrderIds);

        if (clustersById == null) {
            clustersById = loadClusterNamesById(client);
        }

        Map<String, Supply> byBundleId = buildSuppliesByBundleId(accrualsBySupplyId, supplyOrderDtos, clustersById);

        loadSupplyCompositions(byBundleId, client);

        String spreadSheetId = sheetsProperties.getSheets().get(CROSSDOCK_REPORT_SPREADSHEET_KEY);
        String title = buildCrossDockNewSheetTitle(fullPath.getFileName().toString());

        prepareCrossDockSheet(spreadSheetId, title);

        List<List<Object>> rawData = buildCrossDockData(client.getShopName(), accrualsBySupplyId);

        if (rawData.isEmpty()) {
            System.err.println("There is no data for shopName:" + client.getShopName());
            return;
        }

        appendCrossDockData(spreadSheetId, title, rawData);
    }

    private String buildCrossDockRange(String title, int startRow, int dataSize) {
        int endRow = startRow + dataSize - 1;
        return "'" + title + "'" + "!A" + startRow + ":H" + endRow;
    }

    private List<List<Object>> buildCrossDockData(String shopName, Map<String, PostingAccrual> accrualsBySupplyId) {
        List<List<Object>> result = new ArrayList<>();

        for (PostingAccrual accrual : accrualsBySupplyId.values()) {
            int totalItemsQuantity = accrual.getSupply().getComposition().getItems().stream().mapToInt(Item::getQuantity).sum();
            BigDecimal sum = accrual.getSum();
            BigDecimal perItem = sum.divide(new BigDecimal(totalItemsQuantity), RoundingMode.FLOOR);
            for (Item item : accrual.getSupply().getComposition().getItems()) {
                List<Object> row = List.of(
                        shopName,
                        accrual.getSupplyId(),
                        accrual.getSupply().getClusterName(),
                        item.getSku(),
                        item.getArticle(),
                        item.getQuantity(),
                        accrual.getSum(),
                        perItem
                );
                result.add(row);
            }
        }
        return result;
    }

    private List<List<Object>> getCrossDockColumnHeadingData() {
        return List.of(List.of("Название магазина", "Номер поставки", "Кластер",
                "SKU ", "Артикул", "Количество", "Сумма", "Расход на товар"));
    }

    // add regex to parse name
    private String buildCrossDockNewSheetTitle(String fileName) {
        LocalDate endOfPeriod = LocalDate.parse(fileName.replace(".xlsx", "").split("-")[1],
                DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return endOfPeriod.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))
                + " "
                + endOfPeriod.getYear();
    }

    private List<PostingAccrualDto> buildAccrualsDtos(List<List<String>> excelList) {
        List<PostingAccrualDto> result = new ArrayList<>();
        for (List<String> list : excelList) {
            PostingAccrualDto dto = new PostingAccrualDto();
            dto.setSupplyId(list.get(ACCRUAL_REPORT_SUPPLY_ID_FIELD_INDEX));
            dto.setSum(list.get(ACCRUAL_REPORT_SUM_FIELD_INDEX));
            dto.setType(list.get(ACCRUAL_REPORT_TYPE_FIELD_INDEX));
            dto.setCargoSpaceCount(list.get(ACCRUAL_REPORT_CARGO_SPACE_COUNT_FIELD_INDEX));
            result.add(dto);
        }
        return result;
    }

    private List<PostingAccrual> buildCrossDockAccruals(List<PostingAccrualDto> dtos, String shopName, Path fullPath) {
        List<PostingAccrual> result = new ArrayList<>();
        for (PostingAccrualDto postingAccrualDto : dtos) {
            PostingAccrual accrual;
            try {
                accrual = postingAccrualMapper.mapToModel(postingAccrualDto);
                result.add(accrual);
            } catch (IllegalArgumentException e) {
                System.err.println("ShopName: " + shopName);
                System.err.println("FileName: " + fullPath);
                System.err.println("postingAccrualDto: " + postingAccrualDto);
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    private Map<String, PostingAccrual> aggregateAccrualsBySupplyId(List<PostingAccrual> accruals) {
        Map<String, PostingAccrual> result = new HashMap<>();
        for (PostingAccrual accrual : accruals) {
            result.compute(accrual.getSupplyId(), (s, postingAccrual) -> {
                if (postingAccrual == null) {
                    PostingAccrual aggregate = new PostingAccrual();
                    aggregate.setSupplyId(accrual.getSupplyId());
                    aggregate.setSum(accrual.getSum());
                    return aggregate;
                }

                postingAccrual.setSum(
                        postingAccrual.getSum().add(accrual.getSum())
                );

                return postingAccrual;
            });
        }
        return result;
    }

    private List<String> getAllSupplyOrderIds(OzonClient client) throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();
        SupplyOrdersPage page;
        String lastId = null;
        do {
            page = client.getSupplyOrdersIds(lastId, SupplyState.COMPLETED);
            result.addAll(page.orderIds());
            lastId = page.nextCursor();
            Thread.sleep(1000);
        } while (page.orderIds().size() >= 100);

        return result;
    }

    private List<SupplyOrderDto> getSupplyOrderDtos(OzonClient client, List<String> supplyOrderIds) throws IOException, InterruptedException {
        int orderIdsMaxLimit = 50;
        List<SupplyOrderDto> result = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i += orderIdsMaxLimit) {
            int to = Math.min(i + orderIdsMaxLimit, supplyOrderIds.size());
            result.addAll(client.getSupplyOrders(supplyOrderIds.subList(i, to)));
            Thread.sleep(1000);
        }
        return result;
    }

    private Map<Long, String> loadClusterNamesById(OzonClient client) throws IOException, InterruptedException {
        Map<Long, String> result = new HashMap<>();
        List<ClusterDto> clusters = new ArrayList<>();

        clusters.addAll(client.getClusters(ClusterType.CLUSTER_TYPE_OZON));
        clusters.addAll(client.getClusters(ClusterType.CLUSTER_TYPE_CIS));

        clusters.forEach(clusterDto -> result.put(clusterDto.getMacrolocalClusterId(), clusterDto.getName()));

        return result;
    }

    private Map<String, Supply> buildSuppliesByBundleId(Map<String, PostingAccrual> accrualsBySupplyId, List<SupplyOrderDto> supplyOrderDtos,
                                                        Map<Long, String> clustersById) {
        Map<String, Supply> result = new HashMap<>();
        for (SupplyOrderDto dto : supplyOrderDtos) {
            for (SupplyInfoDto infoDto : dto.getSupplies()) {
                String supplyId = infoDto.getSupplyId();
                if (!accrualsBySupplyId.containsKey(supplyId)) {
                    continue;
                }
                Supply supply = new Supply();
                supply.setCreatedDate(dto.getCreationDate());
                supply.setOrderId(dto.getOrderId());
                supply.setOrderNumber(dto.getOrderNumber());
                supply.setState(infoDto.getSupplyState());
                supply.setBundleId(infoDto.getBundleId());
                supply.setSupplyId(supplyId);
                supply.setClusterName(clustersById.get(infoDto.getClusterId()));

                result.put(supply.getBundleId(), supply);

                accrualsBySupplyId.get(supplyId).setSupply(supply);
            }
        }

        return result;
    }

    private void loadSupplyCompositions(Map<String, Supply> byBundleId, OzonClient client) throws IOException, InterruptedException {
        for (String bundleId : byBundleId.keySet()) {
            SupplyOrderCompositionDto compositionDto = client.getSupplyOrdersComposition(List.of(bundleId));
            byBundleId.get(bundleId).setComposition(compositionMapper.mapToModel(compositionDto));
            Thread.sleep(300);
        }
    }

    private void prepareCrossDockSheet(String spreadSheetId, String title) throws IOException {
        int sheetId = googleClient.hasSheet(spreadSheetId, title);
        if (sheetId < 0) {
            sheetId = googleClient.createSheet(spreadSheetId, title);
            googleClient.writeTable(getCrossDockColumnHeadingData(),
                    spreadSheetId, "'" + title + "'" + "!A1:H1");
            googleClient.formatCrossDockSheet(spreadSheetId, sheetId);
        }
    }

    public void appendCrossDockData(String spreadSheetId, String title, List<List<Object>> rawData) throws IOException {
        //      find next empty row
        List<List<Object>> table = googleClient.readTable(spreadSheetId, "'" + title + "'");
        int nextEmptyRowNumber = sheetAnalyzer.findNextEmptyRowNumber(table);
//      build writingRange
        String range = buildCrossDockRange(title, nextEmptyRowNumber, rawData.size());
//      populate data
        googleClient.writeTable(rawData, spreadSheetId, range);
    }

    public void updateDailyReport(boolean weekly) throws Exception {
        LocalDate now = LocalDate.now();
        // get sku
        Map<String, List<String>> clientSkuMap = readClientIdAndSkus();

        // get stock data
        Map<String, StockItem> baseStockMap = getStringStockItemMap(clientSkuMap); // (sku, stockItem)
        System.out.println("База создана: " + baseStockMap.size() + " SKU из таблицы");

        //get and aggregate fbo stocks
        clientSkuMap.forEach((clientId, skus) -> {
            try {
                List<StockDto> stocks = loadAggregatedStocks(clientId, skus);

                applyStocks(baseStockMap, stocks);

                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                System.err.println("Ошибка получения остатков для клиента " + clientId + ": " + e.getMessage());
            }
        });

        //get and aggregate postings
        Instant from = now.minusWeeks(3).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = now.atStartOfDay().toInstant(ZoneOffset.UTC);

        List<String> deliverySchemas = List.of("fbo");

        Map<String, Integer> totalSalesForLastThreeWeeks = new HashMap<>();
        Map<String, Integer> totalSalesForLastWeek = new HashMap<>();
        Map<String, Integer> totalSalesForYesterday = new HashMap<>();

        LocalDateTime startOfTheLastWeek = now.minusWeeks(1).atStartOfDay();
        LocalDateTime startOfYesterday = now.minusDays(1).atStartOfDay();
        LocalDateTime startOfToday = now.atStartOfDay();

        for (String clientId : clientSkuMap.keySet()) {
            OzonClient client = clients.get(clientId);
            if (client == null) {
                System.err.println(
                        "Для clientId " + clientId + " не найден OzonClient"
                );
                continue;
            }
            try {
                List<PostingDto> postingsForLastThreeWeeks = loadPostingDtos(from, to, deliverySchemas, client);

                List<PostingDto> postingsForLastWeek = filterPostingsForPeriod(postingsForLastThreeWeeks, startOfTheLastWeek, startOfToday);
                List<PostingDto> postingsForYesterday = filterPostingsForPeriod(postingsForLastThreeWeeks, startOfYesterday, startOfToday);

                Map<String, Integer> salesBySkuForLastThreeWeeks = aggregatePostingsBySku(postingsForLastThreeWeeks);
                mergeSalesBySku(salesBySkuForLastThreeWeeks, totalSalesForLastThreeWeeks);

                Map<String, Integer> salesBySkuForLastWeek = aggregatePostingsBySku(postingsForLastWeek);
                mergeSalesBySku(salesBySkuForLastWeek, totalSalesForLastWeek);

                Map<String, Integer> salesBySkuForYesterday = aggregatePostingsBySku(postingsForYesterday);
                mergeSalesBySku(salesBySkuForYesterday, totalSalesForYesterday);

            } catch (IOException | CsvException e) {
                System.err.println("Ошибка обработки магазина " + client.getShopName() + ": " + e.getMessage());
            } catch (ReportCreatingException e) {
                System.err.println(
                        "Не удалось сформировать отчёт магазина "
                                + client.getShopName()
                                + ": "
                                + e.getMessage()
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Получение отчёта было прервано", e);
            }
        }

        applySalesForLastThreeWeek(baseStockMap, totalSalesForLastThreeWeeks);
        applySalesForYesterday(baseStockMap, totalSalesForYesterday);

        if (weekly) applySalesForLastWeek(baseStockMap, totalSalesForLastWeek);

        // 5. Превращаем карту обратно в список (порядок сохранится благодаря LinkedHashMap)
        List<StockItem> resultList = new ArrayList<>(baseStockMap.values());

        System.out.println("Итоговый список для записи: " + resultList.size());

        resultList.forEach(System.out::println);

        writeDailyReport(resultList);

        if (weekly) writeWeeklyReport(resultList);
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
                item.setSellsForYesterday(0);
                item.setSellsForLastWeek(0);
                item.setSellsForLastThreeWeeks(0);

                baseStockMap.put(cleanSku, item);
            }
        }
        return baseStockMap;
    }

    public Map<String, List<String>> readClientIdAndSkus() throws IOException {
        String spreadSheetId = sheetsProperties.getSheets().get(DAILY_REPORT_SPREADSHEET_KEY);

        List<List<Object>> rawData = googleClient.readTable(spreadSheetId, REPORT_RANGE);

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

    public String getRangeForDailyReport() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        int dayIndex = dayOfWeek.getValue() - 1; // Monday=0, Sunday=6

        int startColIndex = 4 + dayIndex * 4; // E=4
        int endColIndex = startColIndex + 3;

        String startCol = colIndexToLetter(startColIndex);
        String endCol = colIndexToLetter(endColIndex);

        // Возвращаем диапазон для всей колонки (без номера строки)
        return startCol + ":" + endCol;
    }

    private void applyStocks(Map<String, StockItem> baseStockMap, List<StockDto> stocks) {
        for (StockDto dto : stocks) {
            String cleanSku = dto.getSku().trim();
            StockItem item = baseStockMap.get(cleanSku);

            if (item != null) {
                item.setArticle(dto.getArticle());
                item.setAvailableStock(dto.getAvailableStock() + dto.getValidStock());
                item.setInTransitStock(dto.getInTransitStock() + dto.getInSupplyStock());
            }
        }
    }

    private List<StockDto> loadAggregatedStocks(String clientId, List<String> skus) throws IOException, InterruptedException {
        return aggregateStocks(clients.get(clientId).getFBOStocks(skus));
    }

    private String getReadyPostingsReport(Instant from, Instant to, List<String> deliverySchemas, OzonClient client) throws ReportCreatingException, IOException, InterruptedException {
        String postingsReportCode = client.createPostingsReportCode(from.toString(), to.toString(), deliverySchemas);

        PostingsReportInfoResult postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
        int attempts = 0;
        int maxAttempts = 20;

        while (!"success".equals(postingsReportFile.getStatus()) && attempts < maxAttempts) {
            if ("failed".equals(postingsReportFile.getStatus())) {
                System.err.println("Отчет для магазина " + "тут будет имя магазина" + " не сформирован: " + postingsReportFile.getError());
                throw new ReportCreatingException(client.getClientId(), "creation error");
            }
            Thread.sleep(5000);
            postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
            attempts++;
        }

        if (!"success".equals(postingsReportFile.getStatus())) {
            System.err.println("Таймаут ожидания отчета для магазина " + "тут будет имя магазина");
            throw new ReportCreatingException(client.getClientId(), "timeout");
        }
        return postingsReportFile.getFile();
    }

    private List<PostingDto> loadPostingDtos(Instant from, Instant to, List<String> deliverySchemas, OzonClient client) throws ReportCreatingException, IOException, InterruptedException, CsvException {
        String postingsReportFile = getReadyPostingsReport(from, to, deliverySchemas, client);

        List<List<String>> postings = csvParser.readCSVFromUrl(postingsReportFile); // raw
        if (postings == null || postings.isEmpty()) {
            System.err.println("Пустой отчет для магазина " + client.getShopName());
            return List.of();
        }

        postings = csvParser.filterCSV(postings, "Отменён"); // filtered

        List<PostingDto> postingDtos = new ArrayList<>();
        for (List<String> tmpPosting : postings) {
            postingDtos.add(postingDtoMapper.mapToModel(tmpPosting));
        }
        return postingDtos;
    }

    private List<PostingDto> filterPostingsForPeriod(List<PostingDto> postings, LocalDateTime from, LocalDateTime to) {
        return postings.stream()
                .filter(postingDto -> {
                    LocalDateTime acceptDate = postingDto.getAcceptDate();
                    return !acceptDate.isBefore(from) && acceptDate.isBefore(to);
                }).toList();
    }

    private Map<String, Integer> aggregatePostingsBySku(List<PostingDto> postings) {
        Map<String, Integer> salesBySku = new HashMap<>();

        postings.forEach(posting -> {
            String sku = posting.getSku().trim();
            salesBySku.merge(sku, posting.getSells(), Integer::sum);
        });

        return salesBySku;
    }

    private void mergeSalesBySku(Map<String, Integer> source, Map<String, Integer> target) {
        source.forEach(
                (sku, sales) ->
                        target.merge(sku, sales, Integer::sum)
        );
    }

    private void applySalesForLastThreeWeek(
            Map<String, StockItem> baseStockMap,
            Map<String, Integer> salesForLastThreeWeeks) {

        salesForLastThreeWeeks.forEach(
                (sku, sells) -> {
                    String cleanSku = sku.trim();
                    StockItem item = baseStockMap.get(cleanSku);
                    if (item != null) {
                        item.setSellsForLastThreeWeeks(sells);
                    }
                }
        );
    }

    private void applySalesForLastWeek(
            Map<String, StockItem> baseStockMap,
            Map<String, Integer> salesForLastWeek) {

        salesForLastWeek.forEach(
                (sku, sells) -> {
                    String cleanSku = sku.trim();
                    StockItem item = baseStockMap.get(cleanSku);
                    if (item != null) {
                        item.setSellsForLastWeek(sells);
                    }
                }
        );
    }

    private void applySalesForYesterday(
            Map<String, StockItem> baseStockMap,
            Map<String, Integer> salesForYesterday) {
        salesForYesterday.forEach(
                (sku, sells) -> {
                    String cleanSku = sku.trim();
                    StockItem item = baseStockMap.get(cleanSku);
                    if (item != null) {
                        item.setSellsForYesterday(sells);
                    }
                }
        );
    }

    private void writeDailyReport(List<StockItem> reportItems) throws Exception {
        String rangeForToday = getRangeForDailyReport();
        List<List<Object>> date = List.of(
                List.of(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        String spreadSheetId = sheetsProperties.getSheets().get(DAILY_REPORT_SPREADSHEET_KEY);

        googleClient.writeTable(date, spreadSheetId, rangeForToday);  // write date

        googleClient.writeDailyReportItems(spreadSheetId, DAILY_REPORT_SHEET_NAME, reportItems); // write data
    }

    private void writeWeeklyReport(List<StockItem> reportItems) throws Exception {
        String rangeForWeeklyReport = getRangeForWeeklyReport();

        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String startOfTheWeek = LocalDate.now().minusWeeks(1).format(startFormatter);
        String endOfTheWeek = LocalDate.now().minusDays(1).format(endFormatter);

        List<List<Object>> date = List.of(
                List.of(startOfTheWeek + "-" + endOfTheWeek));

        String spreadSheetId = sheetsProperties.getSheets().get(WEEKLY_REPORT_SPREADSHEET_KEY);
        String range = WEEKLY_REPORT_SHEET_NAME + "!" + rangeForWeeklyReport;

        googleClient.writeTable(date, spreadSheetId, range);  // write date

        googleClient.writeWeeklyReportItems(spreadSheetId, WEEKLY_REPORT_SHEET_NAME, reportItems, rangeForWeeklyReport);
    }

    private String getRangeForWeeklyReport() throws IOException {
        String spreadSheetId = sheetsProperties.getSheets().get(WEEKLY_REPORT_SPREADSHEET_KEY);

        String range = WEEKLY_REPORT_SHEET_NAME + "!" + "1:1";

        List<List<Object>> values = googleClient.readTable(spreadSheetId, range);

        List<String> firstRow = values.get(0)
                .stream()
                .map(value -> value == null ? "" : value.toString())
                .filter(s -> !s.isEmpty())
                .toList();

        int startColIndex = 3 + firstRow.size() * 3;
        int endColIndex = startColIndex + 3;

        String startCol = colIndexToLetter(startColIndex);
        String endCol = colIndexToLetter(endColIndex);

        return startCol + ":" + endCol;
    }
}
