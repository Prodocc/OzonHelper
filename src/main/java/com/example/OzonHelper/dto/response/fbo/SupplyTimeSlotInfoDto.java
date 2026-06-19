package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SupplyTimeSlotInfoDto {
    @JsonProperty("drop_off_warehouse_timeslots")
    private WarehouseTimeSlotsDto warehouseTimeslots;
    @JsonProperty("requested_date_from")
    private String requestedDateFrom;
    @JsonProperty("requested_date_to")
    private String requestedDateTo;
}
