package com.example.OzonHelper.dto.request.max;

import com.example.OzonHelper.enums.max.ButtonType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ButtonDto {
    @JsonProperty("type")
    private ButtonType type;
    @JsonProperty("text")
    private String text;
    @JsonProperty("payload")
    private String payload;
}
