package com.example.OzonHelper.enums;

public enum OzonApiEndpoint {

    SUPPLY_ORDER_LIST("/v3/supply-order/list"),
    SUPPLY_ORDER_INFO("/v3/supply-order/get"),
    SUPPLY_ORDER_COMPOSITION("/v1/supply-order/bundle"),
    SUPPLY_CLUSTERS_LIST("/v1/cluster/list"),
    FBS_POSTING_LIST("/v3/posting/fbs/list"),
    SUPPLY_DRAFT_CREATE_CROSSDOCK("/v1/draft/crossdock/create"),
    SUPPLY_DRAFT_CREATE_STATUS("/v2/draft/create/info"),
    SUPPLY_TIMESLOT_INFO("/v2/draft/timeslot/info"),
    SUPPLY_CREATE("/v2/draft/supply/create"),
    RETURN_GIVEOUT_GET_PDF("/v1/return/giveout/get-pdf");

    private final String path;

    OzonApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl(String host) {
        return host + this.path;
    }
}
