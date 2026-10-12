package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReturnProductInfoDto {
    @JsonProperty("offer_id")
    private String article;
    @JsonProperty("name")
    private String name;
    @JsonProperty("quantity")
    private int quantity;
}
