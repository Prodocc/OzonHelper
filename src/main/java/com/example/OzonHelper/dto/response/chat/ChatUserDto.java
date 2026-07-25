package com.example.OzonHelper.dto.response.chat;

import com.example.OzonHelper.enums.ChatUserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class ChatUserDto {
    @JsonProperty("id")
    private String chatUserId;
    @JsonProperty("type")
    private ChatUserType type;
}
