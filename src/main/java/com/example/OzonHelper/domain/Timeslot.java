package com.example.OzonHelper.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Timeslot {
    @JsonProperty("from")
    private OffsetDateTime from;
    @JsonProperty("to")
    private OffsetDateTime to;
}
