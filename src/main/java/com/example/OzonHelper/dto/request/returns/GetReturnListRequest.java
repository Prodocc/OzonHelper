package com.example.OzonHelper.dto.request.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class GetReturnListRequest {
    @JsonProperty("filter")
    private GetReturnListFilter filter;
    @JsonProperty("limit")
    private int limit;
}
