package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class GetReturnGiveoutPNGResponse {
    @JsonProperty("file_content")
    private String fileContent;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("content_type")
    private String contentType;
}
