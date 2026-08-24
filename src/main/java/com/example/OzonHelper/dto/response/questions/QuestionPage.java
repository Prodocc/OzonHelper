package com.example.OzonHelper.dto.response.questions;

import java.util.List;

public record QuestionPage(
        List<QuestionDto> questions,
        String lastId,
        boolean hasNext
) {
}
