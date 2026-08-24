package com.example.OzonHelper.dto.response.answers;

import com.example.OzonHelper.enums.AnswerStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDto {
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("id")
    private String id;
    @JsonProperty("published_at")
    private LocalDateTime publishedAt;
    @JsonProperty("sku")
    private long sku;
    @JsonProperty("status_publication")
    private AnswerStatus status;
    @JsonProperty("text")
    private String text;
}
