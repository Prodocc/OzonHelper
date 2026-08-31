package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SendImageRequest {
    @JsonProperty("text")
    private String text;
    @JsonProperty("attachments")
    private List<ImageAttachmentDto> attachments;
    @JsonProperty("notify")
    private boolean notify;
}
