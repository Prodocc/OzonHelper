package com.example.OzonHelper.client;

import com.example.OzonHelper.config.OzonStoreConfig;
import com.example.OzonHelper.domain.mapper.SupplyOrderMapper;
import com.example.OzonHelper.dto.request.fbs.GetFbsPostingListFilter;
import com.example.OzonHelper.dto.request.fbs.GetFbsPostingListRequest;
import com.example.OzonHelper.dto.response.fbs.GetFbsPostingListResponse;
import com.example.OzonHelper.dto.response.fbs.PostingDto;
import com.example.OzonHelper.dto.response.supply.SupplyOrderContentDto;
import com.example.OzonHelper.dto.csv.OzonPostingRow;
import com.example.OzonHelper.dto.request.supply.*;
import com.example.OzonHelper.dto.response.supply.*;
import com.example.OzonHelper.enums.OzonApiEndpoint;
import com.example.OzonHelper.enums.SupplySortStatus;
import com.example.OzonHelper.enums.SupplyState;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

public class OzonClient implements MarketplaceClient {

    private static final String MARKETPLACE_NAME = "OZON";
    private final int SUPPLY_ORDERS_MAX_LIMIT = 100;
    private final int FBS_POSTING_MAX_LIMIT = 100;

    private final String clientId;
    private final String apiKey;
    private final String apiHost;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String supplierDetails;


    public OzonClient(OzonStoreConfig config, String ozonApiHost, HttpClient httpClient, ObjectMapper objectMapper, SupplyOrderMapper supplyOrderMapper) {
        this.httpClient = httpClient;
        this.mapper = objectMapper;
        this.apiHost = ozonApiHost;
        this.clientId = config.getClientId();
        this.apiKey = config.getApiKey();
        this.supplierDetails = config.getName();
    }


    @Override
    public List<OzonPostingRow> getPostings(LocalDate dateFrom, LocalDate dateTo, String[] skus, String... delivery_schema) throws IOException, InterruptedException {
        return List.of();
    }

    @Override
    public String getMarketplaceName() {
        return MARKETPLACE_NAME;
    }

    @Override
    public String getSupplierDetails() {
        return this.supplierDetails;
    }

    private HttpResponse<String> createJsonBodyAndSendRequest(String url, Object JsonRequestBodyObject) throws IOException, InterruptedException {
        String requestJsonBody = mapper.writeValueAsString(JsonRequestBodyObject);

        return sendRequest(url, requestJsonBody);
    }

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

    public List<String> getSupplyOrdersIds(SupplyState... supplyStates) throws IOException, InterruptedException {
        GetSupplyOrdersID request = new GetSupplyOrdersID();
        GetSupplyOrdersFilter filter = new GetSupplyOrdersFilter();

        filter.setStates(Arrays.stream(supplyStates).toList());

        request.setFilter(filter);
        request.setLimit(SUPPLY_ORDERS_MAX_LIMIT);
        request.setSortBy(SupplySortStatus.TIMESLOT_FROM_UTC);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_ORDER_LIST.getFullUrl(apiHost),
                request);
        return mapper.readValue(response.body(), GetSupplyOrdersResponse.class).getSupplyOrderIds();
    }

    public List<SupplyOrderDto> getSupplyOrders(List<String> supplyOrderIds) throws IOException, InterruptedException {
        GetSupplyOrderRequest request = new GetSupplyOrderRequest();
        request.setSupplyOrderIds(supplyOrderIds);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_ORDER_INFO.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetSupplyOrderResponse.class).getOrders();
    }

    public List<SupplyOrderContentDto> getSupplyOrdersContent(List<String> bundleIds) throws IOException, InterruptedException {
        GetSupplyOrdersCompositionRequest request = new GetSupplyOrdersCompositionRequest();

        request.setBundleIds(bundleIds);
        request.setLimit(SUPPLY_ORDERS_MAX_LIMIT);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_ORDER_COMPOSITION.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetSupplyOrdersContentResponse.class).getItems();
    }

    public List<PostingDto> getFBSPostingList(LocalDateTime since, LocalDateTime to, String status) throws IOException, InterruptedException {
        GetFbsPostingListRequest request = new GetFbsPostingListRequest();
        GetFbsPostingListFilter filter = new GetFbsPostingListFilter();

        filter.setSince(since.toInstant(ZoneOffset.UTC).toString());
        filter.setTo(to.toInstant(ZoneOffset.UTC).toString());
        filter.setStatus(status);

        request.setFilter(filter);
        request.setLimit(FBS_POSTING_MAX_LIMIT);
        request.setOffset(0);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.FBS_POSTING_LIST.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetFbsPostingListResponse.class).getResult().getPostings();
    }

}
