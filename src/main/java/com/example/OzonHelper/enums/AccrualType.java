package com.example.OzonHelper.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum AccrualType {
    CROSS_DOCK("CrossDock", "Кросс-докинг", 12);

    @Getter
    private final String apiValue;
    private final String description;
    private final int id;

    AccrualType(String apiValue, String description, int id) {
        this.apiValue = apiValue;
        this.description = description;
        this.id = id;
    }

    public static AccrualType fromDescription(String value) {
        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException("Тип начисления не указан");
            return null;
        }

        String normalizedValue = value.trim();

        return Arrays.stream(values())
                .filter(accrualType -> accrualType.description.equalsIgnoreCase(normalizedValue))
                .findFirst()
                .orElse(null);
    }
}
