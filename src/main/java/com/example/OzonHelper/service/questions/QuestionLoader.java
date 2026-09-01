package com.example.OzonHelper.service.questions;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.questions.QuestionDto;
import com.example.OzonHelper.dto.response.questions.QuestionPage;
import com.example.OzonHelper.enums.ozon.QuestionStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionLoader {

    public List<QuestionDto> loadAllQuestions(OzonClient client, Instant from, Instant to, QuestionStatus status) throws IOException, InterruptedException {
        List<QuestionDto> questions = new ArrayList<>();
        QuestionPage page;
        String lastId = null;
        do {
            page = client.getQuestions(from.toString(), to.toString(), lastId, status);
            questions.addAll(page.questions());
            lastId = page.lastId();
            Thread.sleep(1000);
        } while (page.hasNext());

        return questions;
    }
}
