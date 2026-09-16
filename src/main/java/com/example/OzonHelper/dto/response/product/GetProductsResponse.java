package com.example.OzonHelper.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetProductsResponse{
    @JsonProperty("result")
    private GetProductsResult result;
}
