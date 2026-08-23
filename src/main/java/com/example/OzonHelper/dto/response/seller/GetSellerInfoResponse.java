package com.example.OzonHelper.dto.response.seller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetSellerInfoResponse {
    @JsonProperty("subscription")
    private SubscriptionDto subscription;
}
