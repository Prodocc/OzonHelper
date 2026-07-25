package com.example.OzonHelper.dto.response.chat;

import com.example.OzonHelper.enums.ChatStatus;
import com.example.OzonHelper.enums.ChatType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatInfoDto {
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("chat_status")
    private ChatStatus status;
    @JsonProperty("chat_type")
    private ChatType type;
}
