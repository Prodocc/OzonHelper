package com.example.OzonHelper.dto.request.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddButtonRequest {
    @JsonProperty("text")
    private String text;
    @JsonProperty("attachments")
    private List<ButtonAttachmentDto> attachments;
    @JsonProperty("notify")
    private boolean notify;
}
