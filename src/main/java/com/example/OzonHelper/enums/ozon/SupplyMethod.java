package com.example.OzonHelper.enums.ozon;

import lombok.Getter;

public enum SupplyMethod {
    DROPOFF("DROPOFF", "Отгрузка заказов в пункт приёма"),
    PICKUP("PICKUP", "Отгрузка заказов курьру");

    private final String apiValue;
    @Getter
    private final String description;

    SupplyMethod(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
