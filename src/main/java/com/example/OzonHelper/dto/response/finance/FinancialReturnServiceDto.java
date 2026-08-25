package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinancialReturnServiceDto {
    @JsonProperty("total")
    private BigDecimal sum;
    @JsonProperty("items")
    private List<FinancialReturnServiceDetailsDto> details;
}
