package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetFbsPostingListRequest {
    @JsonProperty("filter")
    private GetFbsPostingListFilter filter;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("offset")
    private int offset;
}
