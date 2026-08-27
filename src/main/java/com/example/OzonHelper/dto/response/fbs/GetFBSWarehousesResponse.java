package com.example.OzonHelper.dto.response.fbs;

import com.example.OzonHelper.dto.response.fbo.WarehouseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetFBSWarehousesResponse {
    @JsonProperty("cursor")
    private String cursor;
    @JsonProperty("warehouses")
    private List<FBSWarehouseDto> warehouses;
}
