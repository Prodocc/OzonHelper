package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialReportPaymentDto {
    @JsonProperty("currency_code")
    private String currencyCode;
    @JsonProperty("payment")
    private BigDecimal payment;
}
