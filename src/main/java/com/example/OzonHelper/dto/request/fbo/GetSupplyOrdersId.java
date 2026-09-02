package com.example.OzonHelper.dto.request.fbo;

import com.example.OzonHelper.enums.ozon.SupplySortStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//TODO choose name for this class
@Data
public class GetSupplyOrdersId {
    @JsonProperty("filter")
    private GetSupplyOrdersFilter filter;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("sort_by")
    private SupplySortStatus sortBy;
}
