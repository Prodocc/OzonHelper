package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

//TODO choose name for this class

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSupplyOrdersResponse {
    @JsonProperty("order_ids")
    private List<String> supplyOrderIds;
    @JsonProperty("last_id")
    private String lastId;
}
