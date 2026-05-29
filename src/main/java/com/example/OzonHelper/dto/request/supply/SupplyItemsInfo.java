package com.example.OzonHelper.dto.request.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupplyItemsInfo {
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("sku")
    private long sku;
}
