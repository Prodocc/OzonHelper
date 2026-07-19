package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetStocksResponse {
    @JsonProperty("items")
    private List<StockDto> stocks;
}
