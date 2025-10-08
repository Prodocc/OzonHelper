package com.example.OzonHelper.dto.request.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetSupplyOrderInfoRequest {
    @JsonProperty("order_ids")
    private List<String> supplyOrderIds;
}
