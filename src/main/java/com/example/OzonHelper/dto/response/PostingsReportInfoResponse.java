package com.example.OzonHelper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostingsReportInfoResponse {

    @JsonProperty("result")
    private PostingsReportInfoResult responseResult;

}
