package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DirectSupplyDraftCreateRequest {
    @JsonProperty("cluster_info")
    private Object clusterInfo;
    @JsonProperty("deletion_sku_mode")
    private final String deletionMode = "FULL";
    @JsonProperty("macrolocal_cluster_id")
    private long macrolocalClusterId;
}
