package com.example.OzonHelper.dto.request.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetFinancialReportRequest {
    @JsonProperty("date")
    private FinancialReportPeriodDto period;
    @JsonProperty("page")
    private int pageNumber;
    @JsonProperty("with_details")
    private boolean withDetails;
    @JsonProperty("page_size")
    private int pageSize;
}
