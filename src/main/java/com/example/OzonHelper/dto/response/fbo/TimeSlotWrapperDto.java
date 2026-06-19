package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlotWrapperDto {
    @JsonProperty("timeslot")
    private TimeSlotDto timeslot;
}
