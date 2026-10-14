package com.example.OzonHelper.enums.ozon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PostingStatus {
    AWAITING_REGISTRATION("awaiting_registration", "ожидает регистрации"),
    ACCEPTANCE_IN_PROGRESS("acceptance_in_progress","идёт приёмка"),
    AWAITING_PACKAGING("awaiting_packaging", "ожидает упаковки"),
    AWAITING_DELIVER("awaiting_deliver", "ожидает отгрузки"),
    DELIVERING("delivering","доставляется"),
    DRIVER_PICKUP("driver_pickup","у водителя"),
    DELIVERED("delivered","доставлено");


    @Getter
    @JsonValue
    private final String value;
    @Getter
    private final String description;

    PostingStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
