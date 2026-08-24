package com.example.OzonHelper.dto.response.seller;

import com.example.OzonHelper.enums.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.concurrent.Flow;

@Data
public class SubscriptionDto {
    @JsonProperty("is_premium")
    private boolean premium;
    @JsonProperty("type")
    private SubscriptionType type;
}
