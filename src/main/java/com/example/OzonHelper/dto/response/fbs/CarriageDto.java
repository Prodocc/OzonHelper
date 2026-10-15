package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CarriageDto {
    @JsonProperty("available_actions")
    private List<String> availableActions;
    @JsonProperty("id")
    private long id;
    @JsonProperty("postings_count")
    private int postingsCount;
    @JsonProperty("status")
    private String status;
}
