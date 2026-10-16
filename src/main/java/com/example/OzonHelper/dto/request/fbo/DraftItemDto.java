package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DraftItemDto {
    @JsonProperty("sku")
    private long sku;
    @JsonProperty("quantity")
    private int quantity;
}
