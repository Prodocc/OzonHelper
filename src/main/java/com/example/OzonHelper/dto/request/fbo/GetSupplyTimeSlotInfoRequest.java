package com.example.OzonHelper.dto.request.fbo;

import com.example.OzonHelper.enums.SupplyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetSupplyTimeSlotInfoRequest {
    @JsonProperty("date_from")
    private String dateFrom;
    @JsonProperty("date_to")
    private String dateTo;
    @JsonProperty("draft_id")
    private long draftId;
    @JsonProperty("supply_type")
    private SupplyType supplyType;
    @JsonProperty("selected_cluster_warehouses")
    private List<ClusterAndWarehouseInfoDto> clustersAndWarehousesInfo;
}
