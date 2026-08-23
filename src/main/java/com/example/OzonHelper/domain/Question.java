package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.QuestionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Question {
    private String id;
    private String author;
    private LocalDateTime publishedAt;
    private long sku;
    private String article;
    private QuestionStatus status;
    private String text;
    private List<Answer> answers;
    private int answerCount;
}
