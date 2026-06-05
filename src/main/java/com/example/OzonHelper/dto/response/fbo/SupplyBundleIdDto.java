package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyBundleIdDto {
    @JsonProperty("bundle_id")
    private String bundleId;
}
