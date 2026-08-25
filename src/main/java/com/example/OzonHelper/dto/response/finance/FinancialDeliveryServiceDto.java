package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinancialDeliveryServiceDto {
    @JsonProperty("total")
    private BigDecimal total;
    @JsonProperty("items")
    private List<FinancialDeliveryServiceDetailsDto> details;
}
