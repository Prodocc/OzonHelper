package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialReportDto {
    @JsonProperty("cash_flows")
    private List<FinancialReportInfoDto> reports;
    @JsonProperty("details")
    private List<FinancialReportDetailsDto> details;
    @JsonProperty("page_count")
    private int pageCount;
}
