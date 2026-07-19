package com.example.OzonHelper.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostingsReportCreateResponse {

    @JsonProperty("result")
    private PostingsRepostCreateResult result;
}
