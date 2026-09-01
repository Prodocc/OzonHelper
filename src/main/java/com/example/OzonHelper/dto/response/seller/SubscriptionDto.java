package com.example.OzonHelper.dto.response.seller;

import com.example.OzonHelper.enums.ozon.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubscriptionDto {
    @JsonProperty("is_premium")
    private boolean premium;
    @JsonProperty("type")
    private SubscriptionType type;
}
