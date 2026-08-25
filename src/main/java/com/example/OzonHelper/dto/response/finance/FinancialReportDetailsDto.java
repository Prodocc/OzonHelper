package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinancialReportDetailsDto {
    @JsonProperty("begin_balance_amount")
    private BigDecimal balance;
    @JsonProperty("delivery")
    private FinancialDeliveryDto delivery;
    @JsonProperty("invoice_transfer")
    private BigDecimal invoice;
    @JsonProperty("loan")
    private BigDecimal loan;
    @JsonProperty("payments")
    private List<FinancialReportPaymentDto> payment;
    @JsonProperty("period")
    private FinancialPeriodDto period;
    @JsonProperty("return")
    private FinancialReturnsDto returns;

}
