package com.example.OzonHelper.enums;

import lombok.Getter;

/**
 * Статусы поставок FBO
 */
public enum WarehouseType {
    FULL_FILLMENT("FULL_FILLMENT", "Фулфилмент"),
    EXPRESS_DARK_STORE("EXPRESS_DARK_STORE", "Даркстор"),
    SORTING_CENTER("SORTING_CENTER", "Сортировочный центр"),
    ORDERS_RECEIVING_POINT("ORDERS_RECEIVING_POINT", "Пункт приёма заказов"),
    CROSS_DOCK("CROSS_DOCK", "Кросс-докинг"),
    DISTRIBUTION_CENTER("DISTRIBUTION_CENTER", "Распределительный центр"),
    DELIVERY_POINT("DELIVERY_POINT", "Пункт выдачи заказов");

    private final String apiValue;
    @Getter
    private final String description;

    WarehouseType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
