package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.ozon.AnswerStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private String id;
    private String author;
    private LocalDateTime publishedAt;
    private long sku;
    private AnswerStatus status;
    private String text;
}
