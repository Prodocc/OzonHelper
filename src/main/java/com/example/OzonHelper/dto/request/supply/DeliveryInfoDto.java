package com.example.OzonHelper.dto.request.supply;

import com.example.OzonHelper.dto.response.supply.GetClustersResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryInfoDto {
    @JsonProperty("drop_off_warehouse")
    private GetClustersResponse.WarehouseDto warehouseInfo;
}
