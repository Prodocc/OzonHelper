package com.example.OzonHelper.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Warehouse {
    @JsonProperty("address")
    private String address;
    @JsonProperty("name")
    private String name;
    @JsonProperty("warehouse_id")
    private long warehouseId;
}
