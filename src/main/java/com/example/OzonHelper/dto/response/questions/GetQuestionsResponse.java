package com.example.OzonHelper.dto.response.questions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetQuestionsResponse {
    @JsonProperty("questions")
    private List<QuestionDto> questions;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("has_next")
    private boolean hasNext;
}
