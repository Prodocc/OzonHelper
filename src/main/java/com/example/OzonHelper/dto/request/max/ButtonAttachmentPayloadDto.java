package com.example.OzonHelper.dto.request.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ButtonAttachmentPayloadDto {
    @JsonProperty("buttons")
    private List<List<ButtonDto>> buttons;
}
