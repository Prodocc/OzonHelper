package com.example.OzonHelper.dto.request.supply;

import com.example.OzonHelper.enums.SupplyStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetSupplyOrdersFilter {
    @JsonProperty("states")
    private List<SupplyStatus> states;
}
