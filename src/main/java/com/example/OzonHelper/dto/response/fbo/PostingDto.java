package com.example.OzonHelper.dto.response.fbo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostingDto {
    private String sku;
    private String article;
    private LocalDateTime acceptDate;
    private int sells;
}
