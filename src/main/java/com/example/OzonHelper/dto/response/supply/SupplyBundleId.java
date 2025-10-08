package com.example.OzonHelper.dto.response.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyBundleId {
    @JsonProperty("bundle_id")
    private String bundleId;
}
