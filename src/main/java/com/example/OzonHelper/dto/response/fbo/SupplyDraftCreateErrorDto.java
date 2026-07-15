package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.SupplyDraftCreateErrorMessage;
import com.example.OzonHelper.enums.SupplyDraftCreateErrorReason;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SupplyDraftCreateErrorDto {
    @JsonProperty("error_message")
    private SupplyDraftCreateErrorMessage errorMessage;
    @JsonProperty("error_reasons")
    private SupplyDraftCreateErrorReason errorReason;
    @JsonProperty("items_validation")
    private List<ItemsValidationErrorDto> itemsValidationErrorDtos;
    @JsonProperty("macrolocal_cluster_ids")
    private List<Long> macrolocalClusterIds;
    @JsonProperty("message")
    private String message;
    @JsonProperty("skus")
    private List<Long> skus;
}
