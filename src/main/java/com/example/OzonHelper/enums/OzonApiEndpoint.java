package com.example.OzonHelper.enums;

public enum OzonApiEndpoint {

    SUPPLY_ORDER_LIST("/v3/supply-order/list"),
    SUPPLY_ORDER_INFO("/v3/supply-order/get"),
    SUPPLY_ORDER_COMPOSITION("/v1/supply-order/bundle"),
    FBS_POSTING_LIST("/v3/posting/fbs/list");

    private final String path;

    OzonApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl(String host) {
        return host + this.path;
    }
}
