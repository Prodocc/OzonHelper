package com.example.OzonHelper.dto.response.fbo;

import com.example.OzonHelper.enums.SupplyState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyOrderDto {
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
    private SupplyState supplyState;
    @JsonProperty("supplies")
    private List<SupplyOrderInfoDto> supplies;
    @JsonProperty("state_updated_date")
    private LocalDateTime supplyStateUpdatedDate;
    @JsonProperty("timeslot")
    private TimeSlotWrapperDto timeslotWrapper;
}
