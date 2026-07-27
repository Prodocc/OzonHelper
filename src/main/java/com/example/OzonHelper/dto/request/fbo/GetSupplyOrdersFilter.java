package com.example.OzonHelper.dto.request.fbo;

import com.example.OzonHelper.enums.SupplyState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetSupplyOrdersFilter {
    @JsonProperty("order_number_search")
    private String orderNumber;
    @JsonProperty("states")
    private List<SupplyState> states;
}
