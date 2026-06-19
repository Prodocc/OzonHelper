package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSupplyCrossdockDraftRequest {
    @JsonProperty("cluster_info")
    private ClusterInfoDto clusterInfo;
    @JsonProperty("deletion_sku_mode")
    private final String dsm = "FULL";
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
