package com.example.OzonHelper.dto.response.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetChatListResponse {
    @JsonProperty("chats")
    private List<ChatDto> chats;
    @JsonProperty("total_unread_count")
    private int totalUnreadCount;
    @JsonProperty("cursor")
    private String cursor;
    @JsonProperty("has_next")
    private boolean hasNext;
}
