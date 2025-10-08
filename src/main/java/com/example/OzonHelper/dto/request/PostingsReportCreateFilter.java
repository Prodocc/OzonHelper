package com.example.OzonHelper.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostingsReportCreateFilter {
    @JsonProperty("processed_at_from")
    private String dateFrom;
    @JsonProperty("processed_at_to")
    private String dateTo;
    @JsonProperty("delivery_schema")
    private String[] deliverySchema;
    @JsonProperty("sku")
    private String[] sku;
    @JsonProperty("cancel_reason_id")
    private String[] cancelReasonId;
    @JsonProperty("offer_id")
    private String offerId;
    @JsonProperty("status_alias")
    private String[] statusAlias;
    @JsonProperty("statuses")
    private String[] statuses;
    @JsonProperty("title")
    private String title;

}
