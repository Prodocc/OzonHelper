package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseTimeSlotsDto {
    @JsonProperty("current_time_in_timezone")
    private String currentTimeInTimezone;
    @JsonProperty("days")
    private List<WarehouseTimeSlotByDaysDto> timeslotsByDays;
    @JsonProperty("warehouse_timezone")
    private String warehouseTimezone;
}
