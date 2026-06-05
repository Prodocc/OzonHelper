package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupplyDraftStatusRequest {
    @JsonProperty("draft_id")
    private long draftId;
}
