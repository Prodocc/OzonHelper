package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetClustersRequest {
    @JsonProperty("cluster_type")
    private String clusterType;
}
