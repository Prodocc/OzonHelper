package com.example.OzonHelper.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostingsReportGetRequest {
    @JsonProperty("code")
    private String code;
}
