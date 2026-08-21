package com.example.OzonHelper.dto.request.questions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetQuestionsRequest {
    @JsonProperty("filter")
    private GetQuestionsFilter filter;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("sort_dir")
    private String sortDir;
}
