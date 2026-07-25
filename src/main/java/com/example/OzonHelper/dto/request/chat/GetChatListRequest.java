package com.example.OzonHelper.dto.request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetChatListRequest {
    @JsonProperty("filter")
    private GetChatListFilter filter;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("cursor")
    private String cursor;
}
