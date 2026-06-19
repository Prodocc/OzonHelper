package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.SupplyCreateErrorMessage;
import com.example.OzonHelper.enums.SupplyCreateErrorReason;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SupplyCreateErrorDto {
    @JsonProperty("error_message")
    private SupplyCreateErrorMessage errorMessage;
    @JsonProperty("error_reasons")
    private SupplyCreateErrorReason errorReason;
    @JsonProperty("items_validation")
    private List<ItemsValidationErrorDto> itemsValidationErrorDtos;
    @JsonProperty("macrolocal_cluster_ids")
    private List<Long> macrolocalClusterIds;
    @JsonProperty("message")
    private String message;
    @JsonProperty("skus")
    private List<Long> skus;
}
