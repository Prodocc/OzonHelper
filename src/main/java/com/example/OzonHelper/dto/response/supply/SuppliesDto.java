package com.example.OzonHelper.dto.response.supply;

import com.example.OzonHelper.enums.SupplyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuppliesDto {
    @JsonProperty("is_crossdock")
    private boolean isCrossDock;
    @JsonProperty("bundle_id")
    private String bundleId;
    @JsonProperty("state")
    private SupplyStatus supplyStatus;
    @JsonProperty("storage_warehouse")
    private WarehouseDto receivingWarehouse;
    @JsonProperty("supply_id")
    private long supplyId;
    @JsonProperty("supply_tags.is_utd")
    private boolean isUtd;
}
