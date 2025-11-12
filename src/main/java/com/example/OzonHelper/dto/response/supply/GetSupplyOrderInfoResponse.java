package com.example.OzonHelper.dto.response.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSupplyOrderInfoResponse {
    @JsonProperty("orders")
    private List<SupplyOrderInfoDto> orders;
}
