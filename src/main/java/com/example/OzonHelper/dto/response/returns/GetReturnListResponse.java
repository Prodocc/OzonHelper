package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetReturnListResponse {
    @JsonProperty("returns")
    private List<ReturnDto> returns;
    @JsonProperty("has_next")
    private boolean hasNext;
}
