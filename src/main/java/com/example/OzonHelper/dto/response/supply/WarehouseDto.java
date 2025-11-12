package com.example.OzonHelper.dto.response.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDto {
    @JsonProperty("address")
    private String address;
    @JsonProperty("name")
    private String name;
    @JsonProperty("warehouse_id")
    private long id;
}
