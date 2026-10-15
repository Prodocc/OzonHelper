package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarriageGetPdfResponse {
    @JsonProperty("file_content")
    private String fileContent;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("content_type")
    private String contentType;
}
