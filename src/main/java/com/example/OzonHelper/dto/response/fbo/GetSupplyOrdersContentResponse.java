package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSupplyOrdersContentResponse {
    @JsonProperty("items")
    private List<SupplyOrderContentDto> items;
}
