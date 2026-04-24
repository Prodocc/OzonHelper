package com.example.OzonHelper.dto.request.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetFbsPostingListFilter {
    @JsonProperty("since")
    private String since;
    @JsonProperty("to")
    private String to;
    @JsonProperty("status")
    private String status;
}
