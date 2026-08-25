package com.example.OzonHelper.dto.request.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FinancialReportPeriodDto {
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
}
