package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class GetReturnBarcodePNGResponse {
    @JsonProperty("png")
    private String png;
}
