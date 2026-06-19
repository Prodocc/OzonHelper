package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetSupplyOrderRequest {
    @JsonProperty("order_ids")
    private List<String> supplyOrderIds;
}
