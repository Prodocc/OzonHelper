package com.example.OzonHelper.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetProductsRequest {
    @JsonProperty("filter")
    private GetProductsFilter filter;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("limit")
    private int limit;
}
