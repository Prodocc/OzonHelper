package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.WarehouseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GetClustersResponse {
    @JsonProperty("clusters")
    private List<ClusterDto> clusters;

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WarehouseDto {
        @JsonProperty("name")
        private String name;
        @JsonProperty("warehouse_id")
        private long id;
        @JsonProperty("warehouse_type")
        private WarehouseType warehouseType;
    }
}
