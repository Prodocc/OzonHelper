package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostingDto {
    @JsonProperty("posting_number")
    private String posting_number;
    @JsonProperty("status")
    private String status;
}
