package com.example.OzonHelper.enums.ozon;

import lombok.Getter;

/**
 * Статусы поставок FBO
 */
public enum SupplyState {
    DATA_FILLING("DATA_FILLING", "Заполнение данных"),
    READY_TO_SUPPLY("READY_TO_SUPPLY", "Готова к отгрузке"),
    ACCEPTED_AT_SUPPLY_WAREHOUSE("ACCEPTED_AT_SUPPLY_WAREHOUSE", "Принята на точке отгрузки"),
    IN_TRANSIT("IN_TRANSIT", "В пути"),
    ACCEPTANCE_AT_STORAGE_WAREHOUSE("ACCEPTANCE_AT_STORAGE_WAREHOUSE", "Приёмка на складе"),
    REPORTS_CONFIRMATION_AWAITING("REPORTS_CONFIRMATION_AWAITING", "Согласование актов"),
    REPORT_REJECTED("REPORT_REJECTED", "Спор"),
    COMPLETED("COMPLETED", "Завершена"),
    REJECTED_AT_SUPPLY_WAREHOUSE("REJECTED_AT_SUPPLY_WAREHOUSE", "Отказано в приёмке"),
    CANCELLED("CANCELLED", "Отменена"),
    OVERDUE("OVERDUE", "Просрочена");

    private final String apiValue;
    @Getter
    private final String description;

    SupplyState(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
