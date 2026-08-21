package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.SupplyState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyInfoDto {
    @JsonProperty("is_crossdock")
    private boolean isCrossDock;
    @JsonProperty("macrolocal_cluster_id")
    private long clusterId;
    @JsonProperty("bundle_id")
    private String bundleId;
    @JsonProperty("state")
    private SupplyState supplyState;
    @JsonProperty("storage_warehouse")
    private WarehouseDto receivingWarehouse;
    @JsonProperty("supply_id")
    private String supplyId;
    @JsonProperty("supply_tags.is_utd")
    private boolean isUtd;
}
