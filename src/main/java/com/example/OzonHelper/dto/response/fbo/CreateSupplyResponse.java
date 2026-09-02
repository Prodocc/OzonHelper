package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.ozon.SupplyCreateErrorReason;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSupplyResponse {
    @JsonProperty("draft_id")
    private long draftId;
    @JsonProperty("error_reasons")
    private List<SupplyCreateErrorReason> errorReasons;
}
