package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemsValidationErrorDto {
    @JsonProperty("macrolocal_cluster_id")
    private long macrolocalClusterId;
    @JsonProperty("rejected_items")
    private List<RejectedItemDto> rejectedItems;
}
