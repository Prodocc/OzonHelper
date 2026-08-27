package com.example.OzonHelper.dto.request;

import com.example.OzonHelper.enums.WarehouseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetWarehousesRequest {
    @JsonProperty("warehouse_types")
    private List<WarehouseType> warehouseType;
}
