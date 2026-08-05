package com.example.OzonHelper.dto.response.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccrualDto {
    @JsonProperty("description")
    private String description;
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
}
