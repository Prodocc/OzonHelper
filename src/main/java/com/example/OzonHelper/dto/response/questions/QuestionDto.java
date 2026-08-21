package com.example.OzonHelper.dto.response.questions;

import com.example.OzonHelper.enums.QuestionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDto {
    @JsonProperty("answers_count")
    private int answersCount;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("id")
    private String id;
    @JsonProperty("product_url")
    private String productUrl;
    @JsonProperty("published_at")
    private LocalDateTime publishedAt;
    @JsonProperty("question_link")
    private String questionLink;
    @JsonProperty("sku")
    private long sku;
    @JsonProperty("status")
    private QuestionStatus status;
    @JsonProperty("text")
    private String text;
}
