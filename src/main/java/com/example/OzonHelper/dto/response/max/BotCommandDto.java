package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BotCommandDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;

}
