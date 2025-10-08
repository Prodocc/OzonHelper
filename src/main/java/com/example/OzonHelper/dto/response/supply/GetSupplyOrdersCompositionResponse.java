package com.example.OzonHelper.dto.response.supply;

import com.example.OzonHelper.domain.SupplyOrderItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSupplyOrdersCompositionResponse {
    @JsonProperty("items")
    private List<SupplyOrderItem> items;
}
