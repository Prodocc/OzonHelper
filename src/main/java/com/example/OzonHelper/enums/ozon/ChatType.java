package com.example.OzonHelper.enums.ozon;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ChatType {
    CHAT_TYPE_UNSPECIFIED("UNSPECIFIED", "Не определено"),
    CHAT_TYPE_SELLER_SUPPORT("SELLER_SUPPORT", "Чат с поддержкой"),
    CHAT_TYPE_BUYER_SELLER("BUYER_SELLER", "Чат с покупателем"),
    CHAT_TYPE_BUYER_SELLER_SELECT("BUYER_SELLER_SELECT", "Чат с покупателем по заказам Ozon Селект"),
    CHAT_TYPE_SELLER_API_UPDATES("SELLER_API_UPDATES", "Чат с обновлениями Seller API"),
    CHAT_TYPE_SELLER_API_NOTIFICATIONS("SELLER_API_NOTIFICATIONS", "Чат с уведомлениями Seller API"),
    CHAT_TYPE_SELLER_NOTIFICATION_LOGISTICS("SELLER_NOTIFICATION_LOGISTICS", "Чат с уведомлениями Ozon Доставки"),
    CHAT_TYPE_SELLER_NOTIFICATION_UPDATE_CONTENT("SELLER_NOTIFICATION_UPDATE_CONTENT", "Чат с уведомлениями об изменениях в атрибутно-категорийной модели");


    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    ChatType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
