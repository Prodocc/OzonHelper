package com.example.OzonHelper.dto.request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetChatHistoryRequest {
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("direction")
    private String sortDirection;
    @JsonProperty("filter")
    private GetChatHistoryFilter filter;
    @JsonProperty("from_message_id")
    private long startFromMsgId;
    @JsonProperty("limit")
    private int limit;
}
