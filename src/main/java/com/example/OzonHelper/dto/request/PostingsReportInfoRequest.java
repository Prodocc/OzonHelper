package com.example.OzonHelper.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostingsReportInfoRequest {
    @JsonProperty("code")
    private String code;
}
