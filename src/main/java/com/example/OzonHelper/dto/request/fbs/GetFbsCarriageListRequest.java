package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class GetFbsCarriageListRequest {
    @JsonProperty("filter")
    private GetFbsCarriageListFilter filter;
    @JsonProperty("limit")
    private int limit;
}
