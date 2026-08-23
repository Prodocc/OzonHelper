package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.Question;
import com.example.OzonHelper.dto.response.questions.QuestionDto;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public Question mapToModel(QuestionDto dto) {
        Question question = new Question();
        question.setId(dto.getId());
        question.setAuthor(dto.getAuthorName());
        question.setPublishedAt(dto.getPublishedAt());
        question.setSku(dto.getSku());
        question.setStatus(dto.getStatus());
        question.setText(dto.getText());
        question.setAnswerCount(dto.getAnswersCount());

        return question;
    }
}
