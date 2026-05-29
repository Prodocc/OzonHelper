package com.example.OzonHelper.dto.response.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClusterInfoDto {
    @JsonProperty("warehouses")
    private List<GetClustersResponse.WarehouseDto> warehouses;
}
