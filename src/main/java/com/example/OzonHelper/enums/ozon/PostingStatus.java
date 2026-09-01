package com.example.OzonHelper.enums.ozon;

import lombok.Getter;

public enum PostingStatus {
    AWAITING_PACKAGING("awaiting_packaging", "ожидает упаковки"),
    AWAITING_DELIVER("awaiting_deliver", "ожидает отгрузки");

    @Getter
    private final String value;
    @Getter
    private final String description;

    PostingStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
