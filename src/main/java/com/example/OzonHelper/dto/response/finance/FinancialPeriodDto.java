package com.example.OzonHelper.dto.response.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FinancialPeriodDto {
    @JsonProperty("begin")
    private String start;
    @JsonProperty("end")
    private String end;
    @JsonProperty("id")
    private long id;
}
