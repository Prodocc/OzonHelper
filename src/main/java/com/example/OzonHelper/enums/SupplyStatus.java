package com.example.OzonHelper.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum SupplyStatus {
    ORDER_STATE_DATA_FILLING("ORDER_STATE_DATA_FILLING", "Заполнение данных"),
    ORDER_STATE_READY_TO_SUPPLY("ORDER_STATE_READY_TO_SUPPLY", "Готова к отгрузке"),
    ORDER_STATE_ACCEPTED_AT_SUPPLY_WAREHOUSE("ORDER_STATE_ACCEPTED_AT_SUPPLY_WAREHOUSE", "Принята на точке отгрузки"),
    ORDER_STATE_IN_TRANSIT("ORDER_STATE_IN_TRANSIT", "В пути"),
    ORDER_STATE_ACCEPTANCE_AT_STORAGE_WAREHOUSE("ORDER_STATE_ACCEPTANCE_AT_STORAGE_WAREHOUSE", "Приёмка на складе"),
    ORDER_STATE_REPORTS_CONFIRMATION_AWAITING("ORDER_STATE_REPORTS_CONFIRMATION_AWAITING", "Согласование актов"),
    ORDER_STATE_REPORT_REJECTED("ORDER_STATE_REPORT_REJECTED", "Спор"),
    ORDER_STATE_COMPLETED("ORDER_STATE_COMPLETED", "Завершена"),
    ORDER_STATE_REJECTED_AT_SUPPLY_WAREHOUSE("ORDER_STATE_REJECTED_AT_SUPPLY_WAREHOUSE", "Отказано в приёмке"),
    ORDER_STATE_CANCELLED("ORDER_STATE_CANCELLED", "Отменена");

    private final String apiValue;
    @Getter
    private final String description;

    SupplyStatus(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }

    public String getApiValue() {
        return apiValue;
    }

    public static Optional<SupplyStatus> fromApiValue(String apiValue) {
        if (apiValue == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(supplyStatus -> supplyStatus.apiValue.equalsIgnoreCase(apiValue))
                .findFirst();
    }
}
