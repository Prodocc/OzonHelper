package com.example.OzonHelper.enums.ozon;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ChatUserType {
    CHAT_USER_TYPE_CUSTOMER("Customer", "Покупатель"),
    CHAT_USER_TYPE_SELLER("Seller", "Продавец"),
    CHAT_USER_TYPE_CRM("Crm", "Системные сообщения"),
    CHAT_USER_TYPE_COURIER("Courier", "Курьер"),
    CHAT_USER_TYPE_SUPPORT("Support", "Поддержка"),
    CHAT_USER_TYPE_NOTIFICATION_USER("NotificationUser", "Уведомления");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    ChatUserType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
