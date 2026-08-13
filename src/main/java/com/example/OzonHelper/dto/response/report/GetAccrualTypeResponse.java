package com.example.OzonHelper.dto.response.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetAccrualTypeResponse {
    @JsonProperty("accrual_types")
    private List<AccrualDto> accruals;
}
