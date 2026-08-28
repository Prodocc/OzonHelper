package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBotInfoResponse {
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("is_bot")
    private boolean isBot;
    @JsonProperty("description")
    private String description;
    @JsonProperty("commands")
    private List<BotCommandDto> commands;
}
