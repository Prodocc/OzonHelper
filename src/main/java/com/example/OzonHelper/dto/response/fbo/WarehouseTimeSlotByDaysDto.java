package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseTimeSlotByDaysDto {
    @JsonProperty("date_in_timezone")
    private String dateInTimezone;
    @JsonProperty("timeslots")
    private List<TimeSlotDto> timeSlots;

    @Data
    public static class TimeSlotDto {
        @JsonProperty("from_in_timezone")
        private String from;
        @JsonProperty("to_in_timezone")
        private String to;
    }
}
