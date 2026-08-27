package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FBSWarehouseDto {
    @JsonProperty("address_info")
    private AddressInfoDto addressInfo;
    @JsonProperty("name")
    private String name;
    @JsonProperty("warehouse_id")
    private long id;
}
