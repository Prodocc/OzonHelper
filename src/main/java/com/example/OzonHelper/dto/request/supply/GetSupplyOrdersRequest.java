package com.example.OzonHelper.dto.request.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetSupplyOrdersRequest {

    @JsonProperty("filter")
    private GetSupplyOrdersFilter filter;

    @JsonProperty("paging")
    private GetSupplyOrdersPaging paging;
}
