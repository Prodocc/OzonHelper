package com.example.OzonHelper.dto.request.fbo;

import com.example.OzonHelper.enums.ozon.SupplyMethod;
import com.example.OzonHelper.enums.ozon.WarehouseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryInfoDto {
    @JsonProperty("drop_off_warehouse")
    private WarehouseDto warehouseInfo;
    @JsonProperty("type")
    private SupplyMethod method;

    @Data
    public static class WarehouseDto {
        @JsonProperty("warehouse_id")
        private long id;
        @JsonProperty("warehouse_type")
        private WarehouseType warehouseType;
    }
}
