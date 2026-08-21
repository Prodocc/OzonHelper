package com.example.OzonHelper.dto.request.questions;

import com.example.OzonHelper.enums.QuestionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetQuestionsFilter {
    @JsonProperty("date_from")
    private String dateFrom;
    @JsonProperty("date_to")
    private String dateTo;
    @JsonProperty("status")
    private QuestionStatus status;
}
