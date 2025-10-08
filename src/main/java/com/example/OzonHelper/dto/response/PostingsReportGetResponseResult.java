package com.example.OzonHelper.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostingsReportGetResponseResult {
    @JsonProperty("status")
    private String status;
    @JsonProperty("file")
    private String file;
}
