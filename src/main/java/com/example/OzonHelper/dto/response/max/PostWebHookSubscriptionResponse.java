package com.example.OzonHelper.dto.response.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostWebHookSubscriptionResponse {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("message")
    private String errorMessage;
}
