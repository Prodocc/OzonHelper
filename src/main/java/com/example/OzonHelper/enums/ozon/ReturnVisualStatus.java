package com.example.OzonHelper.enums.ozon;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ReturnVisualStatus {
    RETURN_VISUAL_STATUS_ARRIVED_AT_RETURN_PLACE(
            "ArrivedAtReturnPlace", "В пункте выдачи");


    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    ReturnVisualStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
