package com.example.OzonHelper.dto.request.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSupplyOrdersCompositionRequest {
    @JsonProperty("bundle_ids")
    private List<String> bundleIds;
    @JsonProperty("limit")
    private int limit;
}
