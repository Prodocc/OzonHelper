package com.example.OzonHelper.dto.response.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetChatHistoryResponse {
    @JsonProperty("has_next")
    private boolean hasNext;
    @JsonProperty("messages")
    private List<MessageDto> messages;
}
