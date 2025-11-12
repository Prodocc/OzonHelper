package com.example.OzonHelper.dto.response.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlotDto {
    @JsonProperty("from")
    private LocalDateTime from;
    @JsonProperty("to")
    private LocalDateTime to;
}
