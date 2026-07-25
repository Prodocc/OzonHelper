package com.example.OzonHelper.client;

import com.example.OzonHelper.config.OzonStoreConfig;
import com.example.OzonHelper.domain.TimeSlotInterval;
import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.dto.request.PostingsReportCreateFilter;
import com.example.OzonHelper.dto.request.PostingsReportCreateRequest;
import com.example.OzonHelper.dto.request.PostingsReportInfoRequest;
import com.example.OzonHelper.dto.request.fbs.GetFbsPostingListFilter;
import com.example.OzonHelper.dto.request.fbs.GetFbsPostingListRequest;
import com.example.OzonHelper.dto.response.PostingsReportCreateResponse;
import com.example.OzonHelper.dto.response.PostingsReportInfoResponse;
import com.example.OzonHelper.dto.response.PostingsReportInfoResult;
import com.example.OzonHelper.dto.response.fbs.GetFbsPostingListResponse;
import com.example.OzonHelper.dto.response.fbs.PostingDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderContentDto;
import com.example.OzonHelper.dto.csv.OzonPostingRow;
import com.example.OzonHelper.dto.request.fbo.*;
import com.example.OzonHelper.dto.response.fbo.*;
import com.example.OzonHelper.enums.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

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

//TODO: split from ozonclient to -> FBSClient/FBOClient

@Data
public class OzonClient implements MarketplaceClient {

    private final int SUPPLY_ORDERS_MAX_LIMIT = 100;
    private final int FBS_POSTING_MAX_LIMIT = 100;
    private final String clientId;
    private final String apiKey;
    private final String apiHost;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String shopName;


    public OzonClient(OzonStoreConfig config, String ozonApiHost, HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.mapper = objectMapper;
        this.apiHost = ozonApiHost;
        this.clientId = config.getClientId();
        this.apiKey = config.getApiKey();
        this.shopName = config.getName();
    }


    @Override
    public List<OzonPostingRow> getPostings(LocalDate dateFrom, LocalDate dateTo, String[] skus, String... delivery_schema) throws IOException, InterruptedException {
        return List.of();
    }

    @Override
    public String getShopName() {
        return this.shopName;
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

    public List<PostingDto> getFbsPostingList(LocalDateTime since, LocalDateTime to, String status) throws IOException, InterruptedException {
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

    public boolean fbsHasPostings(LocalDateTime since, LocalDateTime to, String status) throws IOException, InterruptedException {
        return getFbsPostingList(since, to, status).size() > 1;
    }

    public List<ClusterDto> getClusters() throws IOException, InterruptedException {
        GetClustersRequest request = new GetClustersRequest();
        request.setClusterType(ClusterType.CLUSTER_TYPE_OZON.toString());

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_CLUSTERS_LIST.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), GetClustersResponse.class).getClusters();
    }

    public long createSupplyCrossdockDraft(long sku, int quantity, Warehouse warehouse) throws IOException, InterruptedException {
        CreateSupplyCrossdockDraftRequest request = new CreateSupplyCrossdockDraftRequest();

        CreateSupplyCrossdockDraftRequest.ClusterInfoDto clusterInfoDto = new CreateSupplyCrossdockDraftRequest.ClusterInfoDto();
        SupplyItemsInfo items = new SupplyItemsInfo();

        items.setSku(sku);
        items.setQuantity(quantity);
        clusterInfoDto.setItems(List.of(items));
        clusterInfoDto.setMacrolocalClusterId(warehouse.getClusterId());

        DeliveryInfoDto deliveryInfoDto = new DeliveryInfoDto();
        DeliveryInfoDto.WarehouseDto warehouseInfo = new DeliveryInfoDto.WarehouseDto();

        warehouseInfo.setId(warehouse.getId());
        warehouseInfo.setWarehouseType(WarehouseType.CROSS_DOCK);
        deliveryInfoDto.setWarehouseInfo(warehouseInfo);
        deliveryInfoDto.setMethod(SupplyMethod.DROPOFF);

        request.setClusterInfo(clusterInfoDto);
        request.setDeliveryInfo(deliveryInfoDto);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_DRAFT_CREATE_CROSSDOCK.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), CreateSupplyCrossdockDraftResponse.class).getDraftId();
    }

    public DraftCreateStatus checkDraftCreateStatus(long draftId) throws IOException, InterruptedException {
        SupplyDraftStatusRequest request = new SupplyDraftStatusRequest();
        request.setDraftId(draftId);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_DRAFT_CREATE_STATUS.getFullUrl(apiHost),
                request);

        return mapper.readValue(response.body(), SupplyDraftStatusResponse.class).getStatus();
    }
    //TODO use cluster id as last param or warehouse???

    public SupplyTimeSlotInfoDto getAvailableTimeSlotsInfo(LocalDate from, LocalDate to, long draftId, SupplyType supplyType, long clusterId) throws IOException, InterruptedException {
        GetSupplyTimeSlotInfoRequest request = new GetSupplyTimeSlotInfoRequest();
        ClusterAndWarehouseInfoDto clusterAndWarehouseInfoDto = new ClusterAndWarehouseInfoDto();
        clusterAndWarehouseInfoDto.setClusterId(clusterId);

        request.setDateFrom(from.toString());
        request.setDateTo(to.toString());
        request.setSupplyType(supplyType);
        request.setDraftId(draftId);
        request.setClustersAndWarehousesInfo(List.of(clusterAndWarehouseInfoDto));

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_TIMESLOT_INFO.getFullUrl(apiHost),
                request
        );

        System.out.println(response.body());

        return mapper.readValue(response.body(), GetSupplyTimeslotInfoResponse.class).getTimeSlotInfo();
    }

    public long createSupply(long draftId, Warehouse warehouse, TimeSlotInterval interval, SupplyType supplyType) throws IOException, InterruptedException {
        CreateSupplyRequest request = new CreateSupplyRequest();
        ClusterAndWarehouseInfoDto clusterAndWarehouseInfoDto = new ClusterAndWarehouseInfoDto();
        clusterAndWarehouseInfoDto.setClusterId(warehouse.getClusterId());

        request.setClusterAndWarehouseInfoDto(List.of(clusterAndWarehouseInfoDto));
        request.setDraftId(draftId);
        request.setTimeslot(new CreateSupplyRequest.TimeSlotDto(interval.getFrom().toString(), interval.getTo().toString()));
        request.setSupplyType(supplyType);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.SUPPLY_CREATE.getFullUrl(apiHost),
                request
        );

        System.out.println(request.getTimeslot());

        System.out.println(response.body());

        return mapper.readValue(response.body(), CreateSupplyResponse.class).getDraftId();
    }

    public List<StockDto> getFBOStocks(List<String> skus) throws IOException, InterruptedException {
        if (skus.isEmpty()) return null;

        GetStocksRequest request = new GetStocksRequest();
        request.setSkus(skus);

        HttpResponse<String> response;

        do {
            response = createJsonBodyAndSendRequest(
                    OzonApiEndpoint.GET_FBO_STOCKS.getFullUrl(apiHost),
                    request
            );
        } while (response.statusCode() != 200);

        return mapper.readValue(response.body(), GetStocksResponse.class).getStocks();
    }


    public String createPostingsReportCode(String from, String to, List<String> deliverySchemas) throws IOException, InterruptedException {
        PostingsReportCreateRequest request = new PostingsReportCreateRequest();
        PostingsReportCreateFilter filter = new PostingsReportCreateFilter();

        filter.setDateFrom(from);
        filter.setDateTo(to);
        filter.setDeliverySchema(deliverySchemas);

        request.setFilter(filter);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.POSTINGS_REPORT_CREATE.getFullUrl(apiHost),
                request
        );

        return mapper.readValue(response.body(), PostingsReportCreateResponse.class).getResult().getCode();
    }

    public PostingsReportInfoResult getPostingsReportInfoByCode(String code) throws IOException, InterruptedException {
        PostingsReportInfoRequest request = new PostingsReportInfoRequest();
        request.setCode(code);

        HttpResponse<String> response = createJsonBodyAndSendRequest(
                OzonApiEndpoint.POSTINGS_REPORT_INFO.getFullUrl(apiHost),
                request
        );

        System.out.println(response);

        return mapper.readValue(response.body(), PostingsReportInfoResponse.class).getResponseResult();
    }


}
