package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetUploadURLResponse {
    @JsonProperty("url")
    private String url;
    @JsonProperty("token")
    private String token;
}
