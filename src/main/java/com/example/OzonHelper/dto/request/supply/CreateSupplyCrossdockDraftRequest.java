package com.example.OzonHelper.dto.request.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateSupplyCrossdockDraftRequest {
    @JsonProperty("cluster_info")
    private CreateSupplyCrossdockDraftRequest.ClusterInfoDto clusterInfo;
    @JsonProperty("delivery_info")
    private DeliveryInfoDto deliveryInfo;

    @Data
    public static class ClusterInfoDto {
        @JsonProperty("items")
        private List<SupplyItemsInfo> items;
        @JsonProperty("macrolocal_cluster_id")
        private long macrolocalClusterId;
    }
}
