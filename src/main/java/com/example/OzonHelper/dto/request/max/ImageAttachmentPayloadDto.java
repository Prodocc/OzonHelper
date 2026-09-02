package com.example.OzonHelper.dto.request.max;

import com.example.OzonHelper.dto.response.max.PhotoUploadDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ImageAttachmentPayloadDto {
    @JsonProperty("url")
    private String url;
    @JsonProperty("token")
    private String token;
    @JsonProperty("photos")
    private Map<String, PhotoUploadDto> photos;
}
