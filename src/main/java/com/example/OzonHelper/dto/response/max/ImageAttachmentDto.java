package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class ImageAttachmentDto {
    @JsonProperty("type")
    private String type;
    @JsonProperty("payload")
    private ImageAttachmentPayloadDto payload;
}
