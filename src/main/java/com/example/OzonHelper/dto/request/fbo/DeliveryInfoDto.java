package com.example.OzonHelper.dto.request.fbo;

import com.example.OzonHelper.dto.response.fbo.GetClustersResponse;
import com.example.OzonHelper.enums.SupplyMethod;
import com.example.OzonHelper.enums.SupplyType;
import com.example.OzonHelper.enums.WarehouseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

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
