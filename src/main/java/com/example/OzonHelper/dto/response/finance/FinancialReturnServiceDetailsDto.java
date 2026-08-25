package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialReturnServiceDetailsDto {
    @JsonProperty("name")
    private String operationName;
    @JsonProperty("price")
    private BigDecimal price;
}
