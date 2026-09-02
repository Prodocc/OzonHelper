package com.example.OzonHelper.dto.request.max;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostWebHookSubscriptionRequest {
    @JsonProperty("url")
    private String url;
    @JsonProperty("update_types")
    private List<Enum> updateTypes;
    @JsonProperty("secret")
    private String secret;
}
