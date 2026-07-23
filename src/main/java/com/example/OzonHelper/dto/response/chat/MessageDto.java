package com.example.OzonHelper.dto.response.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageDto {
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("data")
    private List<String> data;
    @JsonProperty("message_id")
    private long msgId;
    @JsonProperty("user")
    private ChatUserDto chatUser;
}
