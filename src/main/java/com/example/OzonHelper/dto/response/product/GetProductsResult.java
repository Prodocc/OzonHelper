package com.example.OzonHelper.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetProductsResult {
    @JsonProperty("items")
    private List<ProductDto> products;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("total")
    private int total;
}
