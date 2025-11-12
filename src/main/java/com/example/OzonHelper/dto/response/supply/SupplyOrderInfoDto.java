package com.example.OzonHelper.dto.response.supply;

import com.example.OzonHelper.enums.SupplyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyOrderInfoDto {
    @JsonProperty("created_date")
    private LocalDateTime creationDate;
    @JsonProperty("drop_off_warehouse")
    private WarehouseDto shippingWarehouse;
    @JsonProperty("order_id")
    private long orderId;
    @JsonProperty("order_number")
    private String orderNumber;
    @JsonProperty("order_tags")
    private OrderTags orderTags;
    @JsonProperty("state")
    private SupplyStatus supplyStatus;
    @JsonProperty("supplies")
    private List<SuppliesDto> supplies;
    @JsonProperty("timeslot")
    private TimeSlotWrapperDto timeslotWrapper;
}
