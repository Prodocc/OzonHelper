package com.example.OzonHelper.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MaxUpdateType {
    BOT_ADDED("bot_added","Бот добавлен в чат или канал"),
    MESSAGE_CREATED("message_created","Пользователь отправил новое сообщение или опубликовал пост");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    MaxUpdateType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
