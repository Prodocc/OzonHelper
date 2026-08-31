package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class ImageAttachmentPayloadDto {
    @JsonProperty("url")
    private String url;
}
