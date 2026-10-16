package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarriageGetPdfRequest {
    @JsonProperty("id")
    private long carriageId;
}
