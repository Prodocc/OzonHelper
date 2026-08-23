package com.example.OzonHelper.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetProductRequest {
    @JsonProperty("sku")
    private List<Long> skus;
}
