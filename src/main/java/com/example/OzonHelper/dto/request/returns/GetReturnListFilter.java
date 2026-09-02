package com.example.OzonHelper.dto.request.returns;

import com.example.OzonHelper.enums.ozon.ReturnVisualStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetReturnListFilter {
    @JsonProperty("visual_status_name")
    private ReturnVisualStatus status;
    @JsonProperty("return_schema")
    private String returnSchema;
}
