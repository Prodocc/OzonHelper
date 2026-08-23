package com.example.OzonHelper.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductDto {
    @JsonProperty("sku")
    private long sku;
    @JsonProperty("offer_id")
    private String article;
}
