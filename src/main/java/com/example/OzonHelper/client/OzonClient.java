package com.example.OzonHelper.client;

import com.example.OzonHelper.config.OzonStoreConfig;
import com.example.OzonHelper.dto.csv.OzonPostingRow;
import com.example.OzonHelper.dto.request.PostingsReportCreateFilter;
import com.example.OzonHelper.dto.request.PostingsReportCreateRequest;
import com.example.OzonHelper.dto.request.PostingsReportGetRequest;
import com.example.OzonHelper.dto.request.supply.*;
import com.example.OzonHelper.dto.response.PostingsReportCreateResponse;
import com.example.OzonHelper.dto.response.PostingsReportGetResponse;
import com.example.OzonHelper.dto.response.supply.GetSupplyOrderInfoResponse;
import com.example.OzonHelper.dto.response.supply.GetSupplyOrdersCompositionResponse;
import com.example.OzonHelper.dto.response.supply.GetSupplyOrdersResponse;
import com.example.OzonHelper.dto.response.supply.SupplyBundleId;
import com.example.OzonHelper.enums.OzonApiEndpoint;
import com.example.OzonHelper.enums.SupplyStatus;
import com.example.OzonHelper.parser.JacksonCsvParser;
import com.example.OzonHelper.parser.Parser;
import com.example.OzonHelper.util.OzonDateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class OzonClient implements MarketplaceClient {

    private static final String MARKETPLACE_NAME = "OZON";
    private static final String API_ENDPOINT_REPORT_CREATE = "https://api-seller.ozon.ru/v1/report/postings/create";
    private static final String API_ENDPOINT_REPORT_GET = "https://api-seller.ozon.ru/v1/report/info";
    private static final String API_ENDPOINT_SUPPLY_ORDER_LIST = "https://api-seller.ozon.ru/v2/supply-order/list";
    private static final String STATUS_SUCCESS = "SUCCESS";
    //    private static final String STATUS_ERROR = "ERROR";
    private final String FBO_SCHEMA = "fbo";
    private final String[] FBO_SKUS = new String[]{"2687189755"};
    private final String FBS_SCHEMA = "fbs";
    private final String[] FBS_SKUS = new String[]{""};


    private final String clientId;
    private final String apiKey;
    private final String apiHost;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final OzonDateHelper dateHelper = new OzonDateHelper();
    private final Parser<OzonPostingRow> fboParser;
    private final Parser<OzonPostingRow> fbsParser;


    public OzonClient(OzonStoreConfig config, String ozonApiHost, HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.mapper = objectMapper;
        this.apiHost = ozonApiHost;
        this.clientId = config.getClientId();
        this.apiKey = config.getApiKey();
        this.fboParser = new JacksonCsvParser<>(OzonPostingRow.class);
        this.fbsParser = new JacksonCsvParser<>(OzonPostingRow.class);
    }


    @Override
    public List<OzonPostingRow> getPostings(LocalDate dateFrom, LocalDate dateTo, String[] skus, String... delivery_schema) throws IOException, InterruptedException {
        List<OzonPostingRow> resultPostings = new ArrayList<>();

        String[] utcInterval = dateHelper.getUtcInterval(dateFrom, dateTo);

//        for (String schema : delivery_schema) {
//            if (schema.equals(FBO_SCHEMA)) {
//                List<BasePostingRow> postingsFbo = getPostingsFbo(utcInterval[0], utcInterval[1], skus);
//                resultPostings.addAll(postingsFbo);
//            }
//
//            if (schema.equals(FBS_SCHEMA)) {
//                List<BasePostingRow> postingsFbs = getPostingsFbs(utcInterval[0], utcInterval[1], skus);
//                resultPostings.addAll(postingsFbs);
//            }
//        }

        String fboReportCreateJson = getApiEndpointReportCreateJson(utcInterval[0], utcInterval[1], FBO_SKUS, FBO_SCHEMA);
        HttpResponse<String> fboReportCreateResponse = sendRequest(API_ENDPOINT_REPORT_CREATE, fboReportCreateJson);
        System.out.println(fboReportCreateResponse);
        String fboReportCreateResultRequest = getApiEndpointReportCreateResult(fboReportCreateResponse.body());
        String fboReportGetJson = getApiEndpointReportGetJson(fboReportCreateResultRequest);

        HttpResponse<String> fboReportGetResponse;
        PostingsReportGetResponse fboReportPostings;
        String reportUrl = null;


        final int MAX_WAIT_SECONDS = 600;
        final int POLL_INTERVAL_MS = 1000;
        int totalWaitTime = 0;

        while (totalWaitTime < MAX_WAIT_SECONDS * 1000) {
            fboReportGetResponse = sendRequest(API_ENDPOINT_REPORT_GET, fboReportGetJson);
            fboReportPostings = getApiEndpointReportGetResult(fboReportGetResponse.body());
            String status = fboReportPostings.getResponseResult().getStatus();

            if (STATUS_SUCCESS.equalsIgnoreCase(status)) {
                reportUrl = fboReportPostings.getResponseResult().getFile();
                break;
            }

            Thread.sleep(POLL_INTERVAL_MS);
            totalWaitTime += POLL_INTERVAL_MS;
        }
        if (reportUrl == null) {
            throw new RuntimeException("Отчет не был готов в течение " + MAX_WAIT_SECONDS + " секунд (таймаут).");
        }

        String fboCsvFileContent = downloadCsvContent(reportUrl);
        List<OzonPostingRow> fboPostings = fboParser.parse(fboCsvFileContent);

//        List<OzonFbsPostingRow> fbsPostings = fbsParser.parse(csv);

        resultPostings.addAll(fboPostings);
//        resultPostings.addAll(fbsPostings);

        return resultPostings;
    }

    private List<OzonPostingRow> getPostingsFbo(OffsetDateTime dateFrom, OffsetDateTime dateTo, String[] skus) {

        return List.of();
    }

    private List<OzonPostingRow> getPostingsFbs(OffsetDateTime dateFrom, OffsetDateTime dateTo, String[] skus) {
        return List.of();
    }

    @Override
    public String getMarketplaceName() {
        return MARKETPLACE_NAME;
    }


    private HttpResponse<String> sendRequest(String url, Object JsonRequestBodyObject) throws IOException, InterruptedException {
        String requestJsonBody = mapper.writeValueAsString(JsonRequestBodyObject);
        return sendRequest(url, requestJsonBody);
    }

//    private HttpResponse<String> sendPostRequest(String url, String jsonBody) throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Client-Id", clientId)
//                .header("Api-Key", apiKey)
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
//                .build();
//
//        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//    }
//
//    // Новый метод для GET-запросов (например, для скачивания CSV)
//    private HttpResponse<String> sendGetRequest(String url) throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("Client-Id", clientId)
//                .header("Api-Key", apiKey)
//                // Для GET-запроса тело не нужно
//                .GET()
//                .build();
//
//        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//    }


    // TODO: remove later to separate GET and POST requests
    private HttpResponse<String> sendRequest(String url, String requestBodyJson) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Client-Id", clientId)
                .header("Api-Key", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String getApiEndpointReportCreateJson(String dateFrom, String dayTo, String[] skus, String... delivery_schema) throws JsonProcessingException {
        PostingsReportCreateFilter filter = new PostingsReportCreateFilter();
        filter.setDateFrom(dateFrom);
        filter.setDateTo(dayTo);
        filter.setDeliverySchema(delivery_schema);
        filter.setSku(skus);

        PostingsReportCreateRequest request = new PostingsReportCreateRequest();
        request.setFilter(filter);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
    }

    private String getApiEndpointReportCreateResult(String jsonString) {
        try {
            PostingsReportCreateResponse response = mapper.readValue(jsonString, PostingsReportCreateResponse.class);

            return response.getResult().getCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getApiEndpointReportGetJson(String code) throws JsonProcessingException {
        PostingsReportGetRequest request = new PostingsReportGetRequest();
        request.setCode(code);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
    }

    private PostingsReportGetResponse getApiEndpointReportGetResult(String jsonString) {
        try {
            return mapper.readValue(jsonString, PostingsReportGetResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public GetSupplyOrdersResponse getSupplyOrders(List<SupplyStatus> supplyStatuses, int limit) throws IOException, InterruptedException {
        GetSupplyOrdersRequest request = new GetSupplyOrdersRequest();
        GetSupplyOrdersFilter filter = new GetSupplyOrdersFilter();
        GetSupplyOrdersPaging paging = new GetSupplyOrdersPaging();

        filter.setStates(supplyStatuses);
        paging.setLimit(limit);

        request.setFilter(filter);
        request.setPaging(paging);

        HttpResponse<String> response = sendRequest(
                OzonApiEndpoint.SUPPLY_ORDER_LIST.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetSupplyOrdersResponse.class);
    }

    public GetSupplyOrderInfoResponse getSupplyOrdersInfo(List<String> supplyOrderIds) throws IOException, InterruptedException {
        GetSupplyOrderInfoRequest request = new GetSupplyOrderInfoRequest();
        request.setSupplyOrderIds(supplyOrderIds);

        HttpResponse<String> response = sendRequest(
                OzonApiEndpoint.SUPPLY_ORDER_INFO.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetSupplyOrderInfoResponse.class);
    }

    public GetSupplyOrdersCompositionResponse getSupplyOrdersComposition(List<SupplyBundleId> bundleIds, int limit) throws IOException, InterruptedException {
        GetSupplyOrdersCompositionRequest request = new GetSupplyOrdersCompositionRequest();

        request.setBundleIds(bundleIds);
        request.setLimit(limit);

        HttpResponse<String> response = sendRequest(
                OzonApiEndpoint.SUPPLY_ORDER_COMPOSITION.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetSupplyOrdersCompositionResponse.class);
    }

    private String downloadCsvContent(String fileUrl) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("Ошибка при скачивании файла. Статус-код: " + response.statusCode());
        }
    }

}
