package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialReturnsDto {
    @JsonProperty("total")
    private BigDecimal total;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("return_services")
    private FinancialReturnServiceDto returnService;
}
