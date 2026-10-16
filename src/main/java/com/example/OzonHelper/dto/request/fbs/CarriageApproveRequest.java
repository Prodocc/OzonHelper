package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarriageApproveRequest {
    @JsonProperty("carriage_id")
    private long carriageId;
}
