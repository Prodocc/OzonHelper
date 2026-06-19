package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetSupplyTimeslotInfoResponse {
    @JsonProperty("error_reason")
    private String errorReason;
    @JsonProperty("result")
    private SupplyTimeSlotInfoDto timeSlotInfo;

    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
}
