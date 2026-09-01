package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.ozon.ClusterType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClusterDto {
    @JsonProperty("id")
    private long id;
    @JsonProperty("logistic_clusters")
    private List<ClusterInfoDto> clusterInfo;
    @JsonProperty("macrolocal_cluster_id")
    private long macrolocalClusterId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private ClusterType type;
}
