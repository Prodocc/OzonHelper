package com.example.OzonHelper.service.questions;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.Answer;
import com.example.OzonHelper.domain.Question;
import com.example.OzonHelper.domain.StoredQuestion;
import com.example.OzonHelper.domain.mapper.AnswerMapper;
import com.example.OzonHelper.domain.mapper.QuestionMapper;
import com.example.OzonHelper.dto.response.answers.AnswerDto;
import com.example.OzonHelper.dto.response.product.ProductDto;
import com.example.OzonHelper.dto.response.questions.QuestionDto;
import com.example.OzonHelper.dto.response.seller.SubscriptionDto;
import com.example.OzonHelper.enums.QuestionStatus;
import com.example.OzonHelper.enums.SubscriptionType;
import com.example.OzonHelper.util.SheetAnalyzer;
import com.example.OzonHelper.util.SimpleRateLimiter;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final String QUESTIONS_SPREADSHEET_KEY = "questions-table";
    private final int QUESTION_ID_COLUMN_INDEX = 0;
    private final int SKU_COLUMN_INDEX = 2;
    private final int ANSWER_COUNT_COLUMN_INDEX = 6;
    private final String QUESTIONS_COLUMNS_RANGE_START = "A";
    private final String QUESTIONS_COLUMNS_RANGE_END = "H";
    private final SimpleRateLimiter limiter;

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final QuestionLoader questionLoader;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final SheetAnalyzer sheetAnalyzer;

    public QuestionService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetProperties,
                           GoogleClient googleClient, QuestionMapper questionMapper, QuestionLoader questionLoader,
                           AnswerMapper answerMapper, SheetAnalyzer sheetAnalyzer, SimpleRateLimiter limiter) {
        this.clients = clients;
        this.sheetsProperties = sheetProperties;
        this.googleClient = googleClient;
        this.questionLoader = questionLoader;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
        this.sheetAnalyzer = sheetAnalyzer;
        this.limiter = limiter;
    }


    //TODO workaround with rewriting old answers
    public void syncQuestions() throws IOException, InterruptedException, ExecutionException {
        String spreadSheetId = sheetsProperties.getSheets().get(QUESTIONS_SPREADSHEET_KEY);

        Instant from = LocalDateTime.of(2022, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
        Instant to = Instant.now();

        for (OzonClient client : clients.values()) {
            System.out.println("client.getShopName() = " + client.getShopName());

            SubscriptionDto subscriptionInfo = client.getSubscriptionInfo();

            if (subscriptionInfo.getType() != SubscriptionType.PREMIUM_PLUS) continue;

            List<QuestionDto> questionDtos = questionLoader.loadAllQuestions(client, from, to, QuestionStatus.ALL);

            List<Question> questions = questionDtos.stream()
                    .map(questionMapper::mapToModel)
                    .toList();

            System.out.println("questions.size() = " + questions.size());

            Map<String, StoredQuestion> storedQuestionMap = readStoredQuestions(spreadSheetId, client.getShopName());
            System.out.println("storedQuestionMap.keySet().size() = " + storedQuestionMap.size());

            List<Question> questionsToUpdate = findQuestionsToUpdate(questions, storedQuestionMap);
            System.out.println("questionsToUpdate.size() = " + questionsToUpdate.size());

            if (questionsToUpdate.isEmpty()) continue;

            List<ProductDto> productsDtos = client.getProducts(questionsToUpdate.stream()
                    .map(Question::getSku)
                    .distinct()
                    .toList());

            Map<Long, String> articleBySku = productsDtos.stream()
                    .collect(Collectors.toMap(
                            ProductDto::getSku,
                            ProductDto::getArticle
                    ));

            ExecutorService executor = Executors.newFixedThreadPool(5);

            List<Future<?>> futures = new ArrayList<>();

            try {
                for (Question question : questionsToUpdate) {
                    question.setArticle(articleBySku.get(question.getSku()));
                    if (question.getAnswerCount() > 0) {
                        Future<?> future = executor.submit(() -> {
                            try {
                                limiter.acquire();
                                List<AnswerDto> answersDtos = client.getAnswers(question.getId(), question.getSku(), null);
                                List<Answer> answers = answersDtos.stream().map(answerMapper::mapToModel).toList();
                                question.setAnswers(answers);
                                Thread.sleep(40);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        futures.add(future);
                    } else {
                        question.setAnswers(List.of());
                    }
                }
            } finally {
                executor.shutdown();
            }


            for (Future<?> future : futures) {
                future.get();
            }

            List<List<Object>> newRows = new ArrayList<>();
            Map<Integer, List<Object>> rowsToUpdate = new HashMap<>();
            for (Question question : questionsToUpdate) {
                StoredQuestion storedQuestion = storedQuestionMap.get(question.getId());
                if (storedQuestion == null) {
                    newRows.add(buildQuestionRow(question));
                } else {
                    rowsToUpdate.put(storedQuestion.rowNumber(), buildQuestionRow(question));
                }
            }

            System.out.println("newRows.size() = " + newRows.size());
            System.out.println("rowsToUpdate.size() = " + rowsToUpdate.size());

            updateRows(spreadSheetId, client.getShopName(), rowsToUpdate);
            writeNewRows(spreadSheetId, client.getShopName(), newRows);
        }
    }

    private void writeNewRows(String spreadSheetId, String title, List<List<Object>> newRows) throws IOException {
        List<List<Object>> table = googleClient.readTable(spreadSheetId, "'" + title + "'");
        int nextEmptyRowNumber = sheetAnalyzer.findNextEmptyRowNumber(table);
        String range = buildQuestionsRange(title, nextEmptyRowNumber, newRows.size());
        System.out.println(range);
        googleClient.writeTable(newRows, spreadSheetId, range);
    }

    private String buildQuestionsRange(String title, int startRow, int dataSize) {
        int endRow = startRow + dataSize - 1;
        return "'" + title + "'" + "!A" + startRow + ":H" + endRow;
    }

    private void updateRows(String spreadSheetId, String title, Map<Integer, List<Object>> rowsToUpdate) throws IOException {
        if (rowsToUpdate.isEmpty()) return;

        List<ValueRange> valueRanges = googleClient.buildQuestionsDataRanges(title, rowsToUpdate);
        googleClient.writeDataRanges(spreadSheetId, valueRanges);

    }

    private Map<String, StoredQuestion> readStoredQuestions(String spreadSheetId, String shopName) throws IOException {
        int sheetId = googleClient.hasSheet(spreadSheetId, shopName);

        if (sheetId < 0) {
            sheetId = googleClient.createSheet(spreadSheetId, shopName);
            googleClient.writeTable(buildQuestionsHeader(), spreadSheetId,
                    "'" + shopName + "'" + "!" +
                            QUESTIONS_COLUMNS_RANGE_START + "1:" +
                            QUESTIONS_COLUMNS_RANGE_END + "1");
            googleClient.formatQuestionsSheet(spreadSheetId, sheetId);
        }

        Map<String, StoredQuestion> storedQuestionsById = new HashMap<>();

        List<List<Object>> table = googleClient.readTable(spreadSheetId, "'" + shopName + "'");

        if (table.isEmpty()) return Map.of();

        int rowNumber = 2;
        for (List<Object> row : table.subList(1, table.size())) {
            if (!row.isEmpty()) {
                String questionId = String.valueOf(row.get(QUESTION_ID_COLUMN_INDEX));
                long sku = Long.parseLong(String.valueOf(row.get(SKU_COLUMN_INDEX)));
                int answerCount = Integer.parseInt(String.valueOf(row.get(ANSWER_COUNT_COLUMN_INDEX)));

                StoredQuestion storedQuestion = new StoredQuestion(questionId, sku, answerCount, rowNumber);

                storedQuestionsById.put(questionId, storedQuestion);
            }
            rowNumber++;
        }

        return storedQuestionsById;
    }

    private List<Question> findQuestionsToUpdate(List<Question> loadedQuestions, Map<String, StoredQuestion> storedQuestions) {
        List<Question> questionsToUpdate = new ArrayList<>();
        for (Question question : loadedQuestions) {
            StoredQuestion storedQuestion = storedQuestions.get(question.getId());
            if (storedQuestion == null || storedQuestion.answerCount() < question.getAnswerCount()) {
                questionsToUpdate.add(question);
            }
        }

        return questionsToUpdate;
    }

    private List<Object> buildQuestionRow(Question question) {
        List<Object> row = new ArrayList<>();
        row.add(question.getId());
        row.add(question.getPublishedAt().toString());
        row.add(question.getSku());
        row.add(question.getArticle());
        row.add(question.getText());
        row.add(question.getStatus().getDescription());
        row.add(question.getAnswerCount());

        StringBuilder answers = new StringBuilder();
        question.getAnswers().forEach(answer -> {
            answers.append(answer.getText());
            answers.append("\n");
        });
        row.add(answers.toString());

        return row;
    }

    private List<List<Object>> buildQuestionsHeader() {
        return List.of(List.of("QuestionId", "Дата публикации", "sku", "Артикул",
                "Вопрос", "Статус", "Кол-во ответов", "Ответы"));
    }
}
