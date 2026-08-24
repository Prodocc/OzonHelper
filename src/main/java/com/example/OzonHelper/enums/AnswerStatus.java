package com.example.OzonHelper.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AnswerStatus {
    PUBLISHED("PUBLISHED","Опубликован"),
    AWAITING_MODERATION("AWAITING_MODERATION","Ожидает модерации"),
    MODERATION_FAILED("MODERATION_FAILED","Модерация не пройдена"),
    DUPLICATE("DUPLICATE","Дубликат"),
    DELETED("DELETED","Удалён");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    AnswerStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
