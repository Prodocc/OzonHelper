package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialDeliveryDto {
    @JsonProperty("total")
    private BigDecimal total;
    @JsonProperty("amount")
    private BigDecimal sum;
    @JsonProperty("delivery_services")
    private FinancialDeliveryServiceDto deliveryService;
}
