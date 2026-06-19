package com.example.OzonHelper.enums;

public enum SupplyCreateErrorReason {
    UNSPECIFIED("UNSPECIFIED"),
    ALL_ITEMS_COUNT_INVALID("ALL_ITEMS_COUNT_INVALID"),
    ALL_ITEMS_VOLUME_INVALID("ALL_ITEMS_VOLUME_INVALID"),
    ALL_BUNDLES_EMPTY("ALL_BUNDLES_EMPTY"),
    HAS_EMPTY_BUNDLE("HAS_EMPTY_BUNDLE"),
    DISABLED_FOR_SELLER("DISABLED_FOR_SELLER"),
    NO_ACTIVE_SELLER_WAREHOUSE("NO_ACTIVE_SELLER_WAREHOUSE"),
    INVALID_SELLER_WAREHOUSE("INVALID_SELLER_WAREHOUSE");

    private final String apiValue;

    SupplyCreateErrorReason(String apiValue) {
        this.apiValue = apiValue;
    }
    }
