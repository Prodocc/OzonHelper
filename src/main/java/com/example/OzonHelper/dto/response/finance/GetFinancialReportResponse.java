package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetFinancialReportResponse {
    @JsonProperty("result")
    private FinancialReportDto financialReport;
}
