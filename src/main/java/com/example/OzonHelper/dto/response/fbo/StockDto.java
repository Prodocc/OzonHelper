package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StockDto {
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("offer_id")
    private String article;
    @JsonProperty("available_stock_count")
    private int availableStock;
    @JsonProperty("requested_stock_count")
    private int inSupplyStock;
    @JsonProperty("transit_stock_count")
    private int inTransitStock;
    @JsonProperty("valid_stock_count")
    private int validStock;

}
