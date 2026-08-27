package com.example.OzonHelper.enums.ozon;

public enum OzonApiEndpoint {
    PRODUCT_LIST("/v3/product/info/list"),
    SELLER_INFO("/v1/seller/info"),
    SUPPLY_ORDER_LIST("/v3/supply-order/list"),
    SUPPLY_ORDER_INFO("/v3/supply-order/get"),
    SUPPLY_ORDER_COMPOSITION("/v1/supply-order/bundle"),
    SUPPLY_CLUSTERS_LIST("/v1/cluster/list"),
    FBS_POSTING_LIST("/v3/posting/fbs/list"),
    FBS_WAREHOUSES_LIST("/v2/warehouse/list"),
    ALL_WAREHOUSES_LIST("/v1/warehouse/ozon/list"),
    SUPPLY_DRAFT_CREATE_CROSSDOCK("/v1/draft/crossdock/create"),
    SUPPLY_DRAFT_CREATE_STATUS("/v2/draft/create/info"),
    SUPPLY_TIMESLOT_INFO("/v2/draft/timeslot/info"),
    SUPPLY_CREATE("/v2/draft/supply/create"),
    GET_FBO_STOCKS("/v1/analytics/stocks"),
    POSTINGS_REPORT_CREATE("/v1/report/postings/create"),
    POSTINGS_REPORT_INFO("/v1/report/info"),
    CHATS_LIST("/v3/chat/list"),
    CHAT_HISTORY("/v3/chat/history"),
    ACCRUAL_TYPES("/v1/finance/accrual/types"),
    QUESTION_LIST("/v1/question/list"),
    ANSWER_LIST("/v1/question/answer/list"),
    RETURN_LIST("/v1/returns/list"),
    RETURN_NEW_BARCODE_GET_PNG("/v1/return/giveout/barcode-reset"),
    RETURN_BARCODE_GET_PNG("/v1/return/giveout/get-png"),
    RETURN_GIVEOUT_GET_PDF("/v1/return/giveout/get-pdf");

    private final String path;

    OzonApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl(String host) {
        return host + this.path;
    }
}
