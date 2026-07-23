package com.example.OzonHelper.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ChatStatus {
    CHAT_STATUS_ALL("ALL", "Все чаты"),
    CHAT_STATUS_OPENED("OPENED", "Открытые чаты"),
    CHAT_STATUS_CLOSED("CLOSED", "Закрытые чаты"),
    CHAT_STATUS_UNSPECIFIED("UNSPECIFIED", "Не определено");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    ChatStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
