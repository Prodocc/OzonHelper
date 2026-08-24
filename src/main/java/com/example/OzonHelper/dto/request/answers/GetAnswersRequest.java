package com.example.OzonHelper.dto.request.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetAnswersRequest {
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("question_id")
    private String questionId;
    @JsonProperty("sku")
    private long sku;
}
