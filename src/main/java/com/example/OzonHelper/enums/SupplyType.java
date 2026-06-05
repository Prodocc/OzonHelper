package com.example.OzonHelper.enums;

import lombok.Getter;

public enum SupplyType {
    CROSSDOCK("CROSSDOCK","Кросс-докинг"),
    DIRECT("DIRECT","Прямая"),
    MULTI_CLUSTER("MULTI-CLUSTER","Дня нескольких кластеров");

    private final String apiValue;
    @Getter
    private final String description;

    SupplyType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
