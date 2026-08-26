package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReturnDto {
    @JsonProperty("id")
    private long id;
    @JsonProperty("company_id")
    private long companyId;
    @JsonProperty("return_reason_name")
    private String reason;
    @JsonProperty("type")
    private String type;
    @JsonProperty("schema")
    private String schema;
    @JsonProperty("order_id")
    private long orderId;
    @JsonProperty("orderNumber")
    private String orderNumber;
    @JsonProperty("place")
    private ReturnWarehouseDto warehouse;
    @JsonProperty("posting_number")
    private String postingNumber;
    @JsonProperty("product")
    private ReturnProductInfoDto productInfo;
    @JsonProperty("logistic")
    private ReturnLogisticInfoDto logisticInfo;
}
