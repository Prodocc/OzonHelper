package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.ozon.SupplyDraftCreateErrorMessage;
import com.example.OzonHelper.enums.ozon.SupplyDraftCreateErrorReason;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DirectSupplyDraftCreateErrorDto {
    @JsonProperty("error_message")
    private SupplyDraftCreateErrorMessage message;
    @JsonProperty("error_reasons")
    private List<SupplyDraftCreateErrorReason> reasons;
}
