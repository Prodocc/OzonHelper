package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialReportInfoDto {
    @JsonProperty("period")
    private FinancialPeriodDto period;
    @JsonProperty("orders_amount")
    private BigDecimal ordersSum;
    @JsonProperty("returns_amount")
    private BigDecimal returnsSum;
    @JsonProperty("comission_amount")
    private BigDecimal comissionsSum;
    @JsonProperty("services_amount")
    private BigDecimal servicesSum;
    @JsonProperty("item_delivery_and_return_amount")
    private BigDecimal logisticsSum;
    @JsonProperty("currency_code")
    private String currencyCode;
}
