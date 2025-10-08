package com.example.OzonHelper.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostingsReportCreateRequest {
    @JsonProperty("filter")
    private PostingsReportCreateFilter filter;
    @JsonProperty("language")
    private String language = "DEFAULT";
}
