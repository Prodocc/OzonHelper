package com.example.OzonHelper.dto.response.supply;

import com.example.OzonHelper.enums.SupplyCreateRejectedItemReason;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RejectedItemDto {
    @JsonProperty("reasons")
    private List<SupplyCreateRejectedItemReason> rejectedItemReasons;
    @JsonProperty("sku")
    private long sku;
}
