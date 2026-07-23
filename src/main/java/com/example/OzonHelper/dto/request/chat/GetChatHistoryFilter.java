package com.example.OzonHelper.dto.request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetChatHistoryFilter {
    @JsonProperty("message_ids")
    private List<String> MsgIds;
}
