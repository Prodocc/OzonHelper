package com.example.OzonHelper.enums.ozon;

import lombok.Getter;

public enum SupplySortStatus {
    UNSPECIFIED("UNSPECIFIED", "Не определён"),
    ORDER_CREATION("ORDER_CREATION", "По дате создания заявки"),
    ORDER_STATE_UPDATED_AT("ORDER_STATE_UPDATED_AT","По обновлению статуса заявки"),
    TIMESLOT_FROM_UTC("TIMESLOT_FROM_UTC","По таймслоту в UTC"),
    TIMESLOT_FROM_LOCAL("TIMESLOT_FROM_LOCAL","По таймслоту в локальном времени");


    private final String apiValue;
    @Getter
    private final String description;

    SupplySortStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
    }
