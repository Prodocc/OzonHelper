package com.example.OzonHelper.enums.ozon;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum SubscriptionType {
    UNKNOWN("UNKNOWN", "Неизвестный"),
    UNSPECIFIED("UNSPECIFIED", "Нет подписки"),
    PREMIUM("PREMIUM", "Premium"),
    PREMIUM_LITE("PREMIUM_LITE", "Premium Lite"),
    PREMIUM_PLUS("PREMIUM_PLUS", "Premium Plus"),
    PREMIUM_PRO("PREMIUM_PRO", "Premium Pro");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    SubscriptionType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
