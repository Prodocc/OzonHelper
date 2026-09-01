package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class UploadImageResponse {
    @JsonProperty("photos")
    private Map<String, PhotoUploadDto> photos;
}
