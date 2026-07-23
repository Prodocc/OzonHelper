package com.example.OzonHelper.dto.request.chat;

import com.example.OzonHelper.enums.ChatStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetChatListFilter {
    @JsonProperty("chat_status")
    private ChatStatus status;
    @JsonProperty("unread_only")
    private boolean unread;
}
