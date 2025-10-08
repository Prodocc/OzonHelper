package com.example.OzonHelper.dto.response.supply;

import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.enums.SupplyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyOrderInfo {
    @JsonProperty("product_super_fbo")
    private boolean supplyIsSuper;
    @JsonProperty("state")
    private SupplyStatus state;
    @JsonProperty("supplies")
    private List<SupplyBundleId> supplies;
    @JsonProperty("supply_order_id")
    private long supplyOrderId;
    @JsonProperty("supply_order_number")
    private String supplyOrderNumber;
    @JsonProperty("warehouses")
    private List<Warehouse> warehouses;
    @JsonProperty("timeslot")
    private TimeSlotDetails timeslotDetails;
}
