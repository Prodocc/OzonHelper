package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClusterAndWarehouseInfoDto {
    @JsonProperty("macrolocal_cluster_id")
    private long clusterId;
}
