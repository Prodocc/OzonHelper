package com.example.OzonHelper.dto.request.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetSupplyOrdersPaging {
    @JsonProperty("from_supply_order_id")
    private long fromSupplyOrderId;
    @JsonProperty("limit")
    private int limit;
}
