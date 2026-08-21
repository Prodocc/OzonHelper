package com.example.OzonHelper.service.questions;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.StoredQuestion;
import com.example.OzonHelper.dto.response.questions.QuestionDto;
import com.example.OzonHelper.enums.QuestionStatus;
import com.example.OzonHelper.util.SheetAnalyzer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {
    private final String QUESTIONS_SPREADSHEET_KEY = "questions-table";
    private final int QUESTION_ID_COLUMN_INDEX = 0;
    private final int SKU_COLUMN_INDEX = 2;
    private final int ANSWER_COUNT_COLUMN_INDEX = 4;

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final QuestionLoader questionLoader;

    public QuestionService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetProperties,
                           GoogleClient googleClient, SheetAnalyzer sheetAnalyzer, QuestionLoader questionLoader) {
        this.clients = clients;
        this.sheetsProperties = sheetProperties;
        this.googleClient = googleClient;
        this.questionLoader = questionLoader;
    }

    public void SyncQuestions() throws IOException, InterruptedException {
        String spreadSheetId = sheetsProperties.getSheets().get(QUESTIONS_SPREADSHEET_KEY);

        for (OzonClient client : clients.values()) {
//            List<QuestionDto> questionDtos = questionLoader.loadAllQuestions(client, LocalDateTime.now(), LocalDateTime.now(), QuestionStatus.ALL);
            Map<String, StoredQuestion> stringStoredQuestionMap = readStoredQuestions(spreadSheetId, client.getShopName());
            System.out.println(stringStoredQuestionMap);
        }
//        findQuestionsToUpdate()
//        loadAnswers()
//        buildQuestionRows()
    }

    private Map<String, StoredQuestion> readStoredQuestions(String spreadSheetId, String shopName) throws IOException {
        Map<String, StoredQuestion> storedQuestionsById = new HashMap<>();

        List<List<Object>> table = googleClient.readTable(spreadSheetId, "'" + shopName + "'");

        for (List<Object> row : table.subList(1, table.size())) {
            if (!row.isEmpty()) {
                String questionId = String.valueOf(row.get(QUESTION_ID_COLUMN_INDEX));
                long sku = Long.parseLong(String.valueOf(row.get(SKU_COLUMN_INDEX)));
                int answerCount = Integer.parseInt(String.valueOf(row.get(ANSWER_COUNT_COLUMN_INDEX)));

                StoredQuestion storedQuestion = new StoredQuestion(questionId, sku, answerCount);

                storedQuestionsById.put(questionId, storedQuestion);
            }
        }

        return storedQuestionsById;
    }

    public void findQuestionsToUpdate() {

    }

    public void loadAnswers(OzonClient client, List<String> questionIds) {
        client.getAnswers()
    }
}
