package com.example.OzonHelper.dto.response.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatDto {
    @JsonProperty("chat")
    private ChatInfoDto chatInfo;
    @JsonProperty("first_unread_message_id")
    private long firstUnreadMsgId;
    @JsonProperty("last_message_id")
    private long lastUnreadMsgId;
    @JsonProperty("unread_count")
    private int unreadMsgCount;
}
