package com.example.OzonHelper.domain;

import lombok.Data;

public record StoredQuestion(
        String questionId,
        long sku,
        int answerCount,
        int rowNumber
) {

}
