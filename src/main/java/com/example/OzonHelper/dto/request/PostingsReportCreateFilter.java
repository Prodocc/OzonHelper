package com.example.OzonHelper.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostingsReportCreateFilter {
    @JsonProperty("processed_at_from")
    private String dateFrom;
    @JsonProperty("processed_at_to")
    private String dateTo;
    @JsonProperty("delivery_schema")
    private List<String> deliverySchema;
}
