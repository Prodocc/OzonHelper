package com.example.OzonHelper.dto.response;

import com.example.OzonHelper.dto.response.fbo.WarehouseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetWarehousesResponse {
    @JsonProperty("warehouses")
    private List<WarehouseDto> warehouses;
}
