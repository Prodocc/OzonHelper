package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetFBSWarehousesRequest {
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("cursor")
    private String cursor;
}
