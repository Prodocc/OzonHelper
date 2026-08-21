package com.example.OzonHelper.dto.response.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetAnswersResponse {
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("answers")
    private List<AnswerDto> answers;
}
