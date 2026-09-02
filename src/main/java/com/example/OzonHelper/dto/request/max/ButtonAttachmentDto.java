package com.example.OzonHelper.dto.request.max;

import com.example.OzonHelper.enums.max.AttachmentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ButtonAttachmentDto {
    @JsonProperty("type")
    private AttachmentType type;
    @JsonProperty("payload")
    private ButtonAttachmentPayloadDto payload;
}
