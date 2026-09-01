package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.ozon.DraftCreateStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyDraftStatusResponse {
    @JsonProperty("status")
    private DraftCreateStatus status;
}
