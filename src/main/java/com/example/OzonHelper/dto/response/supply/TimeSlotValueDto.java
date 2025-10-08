package com.example.OzonHelper.dto.response.supply;

import com.example.OzonHelper.domain.Timeslot;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlotValueDto {
    @JsonProperty("timeslot")
    private Timeslot timeslot;
}
