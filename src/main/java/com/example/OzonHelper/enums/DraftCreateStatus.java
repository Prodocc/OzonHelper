package com.example.OzonHelper.enums;

import lombok.Getter;

public enum DraftCreateStatus {
    UNSPECIFIED("UNSPECIFIED", "Не определён"),
    SUCCESS("SUCESS", "создан"),
    IN_PROGRESS("IN_PROGRESS", "Создаётся"),
    FAILED("FAILED", "Не удалось создать");

    private final String apiValue;
    @Getter
    private final String description;

    DraftCreateStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
    }
