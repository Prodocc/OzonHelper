package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarriageCreateRequest {
    @JsonProperty("delivery_method_id")
    private long deliveryMethodId;
}
