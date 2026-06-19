package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateSupplyCrossdockDraftResponse {
    @JsonProperty("draft_id")
    private long draftId;
    @JsonProperty("errors")
    private List<SupplyCreateErrorDto> supplyCreateErrors;
}
