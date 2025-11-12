package com.example.OzonHelper.dto.request.supply;

import com.example.OzonHelper.enums.SupplySortStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetSupplyOrdersRequest {

    @JsonProperty("filter")
    private GetSupplyOrdersFilter filter;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("sort_by")
    private SupplySortStatus sortBy;
}
