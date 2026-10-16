package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;

import java.util.List;

@Data
public class DirectSupplyDraftCreateResponse {
    @JsonProperty("draft_id")
    private long draftId;
    @JsonProperty("errors")
    private List<DirectSupplyDraftCreateErrorDto> errors;
}
