package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.dto.response.fbo.PostingDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PostingDtoMapper {

    public PostingDto mapToModel(List<String> posting) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int POSTINGS_SKU_INDEX = 10;
        int POSTINGS_ARTICLE_INDEX = 11;
        int POSTINGS_ACCEPT_DATE_INDEX = 2;
        int POSTINGS_SELLS_INDEX = 18;

        PostingDto dto = new PostingDto();
        dto.setSku(posting.get(POSTINGS_SKU_INDEX));
        dto.setArticle(posting.get(POSTINGS_ARTICLE_INDEX));
        dto.setAcceptDate(LocalDateTime.parse(posting.get(POSTINGS_ACCEPT_DATE_INDEX), formatter));
        dto.setSells(Integer.parseInt(posting.get(POSTINGS_SELLS_INDEX)));

        return dto;
    }
}
