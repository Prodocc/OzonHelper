package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarriageCreateResponse {
    @JsonProperty("carriage_id")
    private long carriageId;
}
