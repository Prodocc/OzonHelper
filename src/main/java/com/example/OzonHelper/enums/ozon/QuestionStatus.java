package com.example.OzonHelper.enums.ozon;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum QuestionStatus {
    NEW("NEW","Новый"),
    ALL("ALL","Все вопросы"),
    VIEWED("VIEWED","Просмотренный"),
    PROCESSED("PROCESSED","Обработанный"),
    UNPROCESSED("UNPROCESSED","Необработанный");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    QuestionStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
