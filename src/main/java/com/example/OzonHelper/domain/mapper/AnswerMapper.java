package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.Answer;
import com.example.OzonHelper.dto.response.answers.AnswerDto;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public Answer mapToModel(AnswerDto dto) {
        Answer answer = new Answer();
        answer.setId(dto.getId());
        answer.setAuthor(dto.getAuthorName());
        answer.setPublishedAt(dto.getPublishedAt());
        answer.setSku(dto.getSku());
        answer.setStatus(dto.getStatus());
        answer.setText(dto.getText());
        return answer;
    }
}
