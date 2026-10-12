package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReturnLogisticInfoDto {
    @JsonProperty("barcode")
    private String labelBarcode;
}
