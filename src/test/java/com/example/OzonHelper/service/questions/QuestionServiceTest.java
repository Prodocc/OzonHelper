package com.example.OzonHelper.service.questions;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.mapper.AnswerMapper;
import com.example.OzonHelper.domain.mapper.QuestionMapper;
import com.example.OzonHelper.dto.response.answers.AnswerDto;
import com.example.OzonHelper.dto.response.product.ProductDto;
import com.example.OzonHelper.dto.response.questions.QuestionDto;
import com.example.OzonHelper.dto.response.questions.QuestionPage;
import com.example.OzonHelper.dto.response.seller.SubscriptionDto;
import com.example.OzonHelper.enums.ozon.AnswerStatus;
import com.example.OzonHelper.enums.ozon.QuestionStatus;
import com.example.OzonHelper.enums.ozon.SubscriptionType;
import com.example.OzonHelper.util.SheetAnalyzer;
import com.example.OzonHelper.util.SimpleRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class QuestionServiceTest {
    private Map<String, OzonClient> clients;
    private GoogleSheetsProperties sheetsProperties;
    private GoogleClient googleClient;
    private QuestionLoader questionLoader;
    private QuestionMapper questionMapper;
    private AnswerMapper answerMapper;
    private SheetAnalyzer sheetAnalyzer;
    private SimpleRateLimiter rateLimiter;
    private QuestionService questionService;

    @BeforeEach
    public void init() {
        clients = Map.of(
                "client-1", mock(OzonClient.class));
        sheetsProperties = mock(GoogleSheetsProperties.class);
        googleClient = mock(GoogleClient.class);
        questionLoader = new QuestionLoader();
        questionMapper = new QuestionMapper();
        answerMapper = new AnswerMapper();
        sheetAnalyzer = new SheetAnalyzer();
        this.rateLimiter = new SimpleRateLimiter();
        questionService = new QuestionService(
                clients, sheetsProperties, googleClient,
                questionMapper, questionLoader, answerMapper,
                sheetAnalyzer, rateLimiter
        );
    }

    @Test
    public void syncQuestions_noStoredQuestions_hasSheet_buildCorrectData() throws IOException, InterruptedException, ExecutionException {
        String shopName = "shopName";

        OzonClient client = clients.get("client-1");
        when(client.getShopName()).thenReturn(shopName);

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setPremium(true);
        subscriptionDto.setType(SubscriptionType.PREMIUM_PLUS);

        when(client.getSubscriptionInfo()).thenReturn(subscriptionDto);

        LocalDateTime publishedAt = LocalDateTime.now();

        List<QuestionDto> questionDtos1 = generateQuestionDtos(0, 4, publishedAt);
        List<QuestionDto> questionDtos2 = generateQuestionDtos(4, 7, publishedAt);
        QuestionPage page1 = new QuestionPage(questionDtos1, "1", true);
        QuestionPage page2 = new QuestionPage(questionDtos2, "", false);

        when(client.getQuestions(anyString(), anyString(), isNull(), any(QuestionStatus.class))).thenReturn(page1);
        when(client.getQuestions(anyString(), anyString(), eq("1"), any(QuestionStatus.class))).thenReturn(page2);

        when(googleClient.hasSheet(anyString(), anyString())).thenReturn(1);

        when(googleClient.readTable(anyString(), eq("'" + "shopName" + "'"))).thenReturn(List.of());

        List<ProductDto> productDtos = generateProductDtos(0, 7);

        when(client.getProducts(anyList())).thenReturn(productDtos);

        List<AnswerDto> answerDtos = generateAnswerDtos(0, 7);

        when(client.getAnswers("0", 0L, null)).thenReturn(List.of(answerDtos.get(0)));
        when(client.getAnswers("1", 1L, null)).thenReturn(List.of(answerDtos.get(1)));
        when(client.getAnswers("2", 2L, null)).thenReturn(List.of(answerDtos.get(2)));
        when(client.getAnswers("3", 3L, null)).thenReturn(List.of(answerDtos.get(3)));
        when(client.getAnswers("4", 4L, null)).thenReturn(List.of(answerDtos.get(4)));
        when(client.getAnswers("5", 5L, null)).thenReturn(List.of(answerDtos.get(5)));
        when(client.getAnswers("6", 6L, null)).thenReturn(List.of(answerDtos.get(6)));

        ArgumentCaptor<List<List<Object>>> newRowsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Map<Integer, List<Object>>> rowsToUpdateCaptor = ArgumentCaptor.forClass(Map.class);

        questionService.syncQuestions();

        verify(googleClient).writeTable(newRowsCaptor.capture(), any(), anyString());
        verify(googleClient, never()).createSheet(anyString(), anyString());

        String time = publishedAt.toString();

        List<List<Object>> newRowsExpected = List.of(
                List.of("0", time, 0L, "article0", "question0", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(0).getText() + "\n"),
                List.of("1", time, 1L, "article1", "question1", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(1).getText() + "\n"),
                List.of("2", time, 2L, "article2", "question2", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(2).getText() + "\n"),
                List.of("3", time, 3L, "article3", "question3", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(3).getText() + "\n"),
                List.of("4", time, 4L, "article4", "question4", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(4).getText() + "\n"),
                List.of("5", time, 5L, "article5", "question5", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(5).getText() + "\n"),
                List.of("6", time, 6L, "article6", "question6", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(6).getText() + "\n")
        );

        List<List<Object>> newRowsActual = newRowsCaptor.getValue();

        assertThat(newRowsActual).isEqualTo(newRowsExpected);
    }

    @Test
    public void syncQuestions_hasStoredQuestions_hasSheet_buildCorrectData() throws IOException, InterruptedException, ExecutionException {
        String shopName = "shopName";

        OzonClient client = clients.get("client-1");
        when(client.getShopName()).thenReturn(shopName);

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setPremium(true);
        subscriptionDto.setType(SubscriptionType.PREMIUM_PLUS);

        when(client.getSubscriptionInfo()).thenReturn(subscriptionDto);

        LocalDateTime publishedAt = LocalDateTime.now();
        String time = publishedAt.toString();

        List<QuestionDto> questionDtos1 = generateQuestionDtos(0, 4, publishedAt);
        List<QuestionDto> questionDtos2 = generateQuestionDtos(4, 7, publishedAt);
        QuestionPage page1 = new QuestionPage(questionDtos1, "1", true);
        QuestionPage page2 = new QuestionPage(questionDtos2, "", false);

        when(client.getQuestions(anyString(), anyString(), isNull(), any(QuestionStatus.class))).thenReturn(page1);
        when(client.getQuestions(anyString(), anyString(), eq("1"), any(QuestionStatus.class))).thenReturn(page2);

        when(googleClient.hasSheet(any(), anyString())).thenReturn(1);

        List<List<Object>> storedQuestions = List.of(
                List.of("questionId", "publicationDate", "sku", "article", "question", "status", "answerCount", "answers"),
                List.of("2", time, 2L, "article2", "question2", QuestionStatus.PROCESSED.getDescription(), 0, "answer2_old" + "\n"),
                List.of("3", time, 3L, "article3", "question3", QuestionStatus.PROCESSED.getDescription(), 0, "answer3_old" + "\n")
        );

        when(googleClient.readTable(any(), eq("'" + "shopName" + "'"))).thenReturn(storedQuestions);

        List<ProductDto> productDtos = generateProductDtos(0, 7);

        when(client.getProducts(anyList())).thenReturn(productDtos);

        List<AnswerDto> answerDtos = generateAnswerDtos(0, 7);

        when(client.getAnswers("0", 0L, null)).thenReturn(List.of(answerDtos.get(0)));
        when(client.getAnswers("1", 1L, null)).thenReturn(List.of(answerDtos.get(1)));
        when(client.getAnswers("2", 2L, null)).thenReturn(List.of(answerDtos.get(2)));
        when(client.getAnswers("3", 3L, null)).thenReturn(List.of(answerDtos.get(3)));
        when(client.getAnswers("4", 4L, null)).thenReturn(List.of(answerDtos.get(4)));
        when(client.getAnswers("5", 5L, null)).thenReturn(List.of(answerDtos.get(5)));
        when(client.getAnswers("6", 6L, null)).thenReturn(List.of(answerDtos.get(6)));

        ArgumentCaptor<List<List<Object>>> newRowsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Map<Integer, List<Object>>> rowsToUpdateCaptor = ArgumentCaptor.forClass(Map.class);

        questionService.syncQuestions();

        verify(googleClient).writeTable(newRowsCaptor.capture(), any(), anyString());
        verify(googleClient).buildQuestionsDataRanges(anyString(), rowsToUpdateCaptor.capture());

        List<List<Object>> newRowsExpected = List.of(
                List.of("0", time, 0L, "article0", "question0", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(0).getText() + "\n"),
                List.of("1", time, 1L, "article1", "question1", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(1).getText() + "\n"),
                List.of("4", time, 4L, "article4", "question4", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(4).getText() + "\n"),
                List.of("5", time, 5L, "article5", "question5", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(5).getText() + "\n"),
                List.of("6", time, 6L, "article6", "question6", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(6).getText() + "\n")
        );

        Map<Integer, List<Object>> rowsToUpdate = Map.of(
                2, List.of("2", time, 2L, "article2", "question2", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(2).getText() + "\n"),
                3, List.of("3", time, 3L, "article3", "question3", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(3).getText() + "\n")
        );

        List<List<Object>> newRowsActual = newRowsCaptor.getValue();
        Map<Integer, List<Object>> rowsToUpdateActual = rowsToUpdateCaptor.getValue();

        assertThat(newRowsActual).isEqualTo(newRowsExpected);
        assertThat(rowsToUpdateActual).isEqualTo(rowsToUpdate);
    }

    @Test
    public void syncQuestions_noStoredQuestions_hasNoSheet_buildCorrectData() throws IOException, InterruptedException, ExecutionException {
        String shopName = "shopName";

        OzonClient client = clients.get("client-1");
        when(client.getShopName()).thenReturn(shopName);

        String spreadSheetId = "spreadsheet-id";
        when(sheetsProperties.getSheets()).thenReturn(Map.of("questions-table", spreadSheetId));

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setPremium(true);
        subscriptionDto.setType(SubscriptionType.PREMIUM_PLUS);

        when(client.getSubscriptionInfo()).thenReturn(subscriptionDto);

        LocalDateTime publishedAt = LocalDateTime.now();

        List<QuestionDto> questionDtos1 = generateQuestionDtos(0, 4, publishedAt);
        List<QuestionDto> questionDtos2 = generateQuestionDtos(4, 7, publishedAt);
        QuestionPage page1 = new QuestionPage(questionDtos1, "1", true);
        QuestionPage page2 = new QuestionPage(questionDtos2, "", false);

        when(client.getQuestions(anyString(), anyString(), isNull(), any(QuestionStatus.class))).thenReturn(page1);
        when(client.getQuestions(anyString(), anyString(), eq("1"), any(QuestionStatus.class))).thenReturn(page2);

        when(googleClient.hasSheet(eq(spreadSheetId), eq(shopName))).thenReturn(-1);

        when(googleClient.readTable(eq(spreadSheetId), eq("'" + "shopName" + "'"))).thenReturn(List.of());

        when(googleClient.createSheet(eq(spreadSheetId), eq(shopName))).thenReturn(123);

        List<ProductDto> productDtos = generateProductDtos(0, 7);

        when(client.getProducts(anyList())).thenReturn(productDtos);

        List<AnswerDto> answerDtos = generateAnswerDtos(0, 7);

        when(client.getAnswers("0", 0L, null)).thenReturn(List.of(answerDtos.get(0)));
        when(client.getAnswers("1", 1L, null)).thenReturn(List.of(answerDtos.get(1)));
        when(client.getAnswers("2", 2L, null)).thenReturn(List.of(answerDtos.get(2)));
        when(client.getAnswers("3", 3L, null)).thenReturn(List.of(answerDtos.get(3)));
        when(client.getAnswers("4", 4L, null)).thenReturn(List.of(answerDtos.get(4)));
        when(client.getAnswers("5", 5L, null)).thenReturn(List.of(answerDtos.get(5)));
        when(client.getAnswers("6", 6L, null)).thenReturn(List.of(answerDtos.get(6)));

        ArgumentCaptor<List<List<Object>>> newRowsCaptor = ArgumentCaptor.forClass(List.class);

        questionService.syncQuestions();

        verify(googleClient, times(2)).writeTable(newRowsCaptor.capture(), eq(spreadSheetId), anyString());
        verify(googleClient).createSheet(eq(spreadSheetId), eq(shopName));
        verify(googleClient).formatQuestionsSheet(eq(spreadSheetId), eq(123));

        String time = publishedAt.toString();

        List<List<Object>> newRowsExpected = List.of(
                List.of("0", time, 0L, "article0", "question0", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(0).getText() + "\n"),
                List.of("1", time, 1L, "article1", "question1", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(1).getText() + "\n"),
                List.of("2", time, 2L, "article2", "question2", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(2).getText() + "\n"),
                List.of("3", time, 3L, "article3", "question3", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(3).getText() + "\n"),
                List.of("4", time, 4L, "article4", "question4", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(4).getText() + "\n"),
                List.of("5", time, 5L, "article5", "question5", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(5).getText() + "\n"),
                List.of("6", time, 6L, "article6", "question6", QuestionStatus.PROCESSED.getDescription(), 1, answerDtos.get(6).getText() + "\n")
        );

        List<List<String>> headerExpected = List.of(List.of("QuestionId", "Дата публикации", "sku", "Артикул",
                "Вопрос", "Статус", "Кол-во ответов", "Ответы"));

        List<List<Object>> headerActual = newRowsCaptor.getAllValues().get(0);
        List<List<Object>> newRowsActual = newRowsCaptor.getAllValues().get(1);

        assertThat(headerActual).isEqualTo(headerExpected);
        assertThat(newRowsActual).isEqualTo(newRowsExpected);
    }

    @Test
    public void syncQuestion_hasNoPremiumPlus_doNothing() throws IOException, InterruptedException {
        String shopName = "shopName";

        OzonClient client = clients.get("client-1");
        when(client.getShopName()).thenReturn(shopName);

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setPremium(true);
        subscriptionDto.setType(SubscriptionType.PREMIUM_LITE);

        when(client.getSubscriptionInfo()).thenReturn(subscriptionDto);

        verifyNoInteractions(googleClient);
    }

    private List<AnswerDto> generateAnswerDtos(int startValue, int amount) {
        List<AnswerDto> result = new ArrayList<>();

        for (int i = startValue; i < amount; i++) {
            AnswerDto dto = new AnswerDto();
            dto.setId(String.valueOf(i));
            dto.setAuthorName("author" + i);
            dto.setPublishedAt(LocalDateTime.now());
            dto.setSku(i);
            dto.setStatus(AnswerStatus.PUBLISHED);
            dto.setText("answer" + i);

            result.add(dto);
        }
        return result;
    }

    private List<ProductDto> generateProductDtos(int startValue, int amount) {
        List<ProductDto> result = new ArrayList<>();

        for (int i = startValue; i < amount; i++) {
            ProductDto dto = new ProductDto();
            dto.setSku(i);
            dto.setArticle("article" + i);

            result.add(dto);
        }

        return result;
    }

    private List<QuestionDto> generateQuestionDtos(int startValue, int amount, LocalDateTime publishedAt) {
        List<QuestionDto> result = new ArrayList<>();

        for (int i = startValue; i < amount; i++) {
            QuestionDto dto = new QuestionDto();
            dto.setAnswersCount(1);
            dto.setAuthorName("author" + i);
            dto.setId(String.valueOf(i));
            dto.setPublishedAt(publishedAt);
            dto.setSku(i);
            dto.setStatus(QuestionStatus.PROCESSED);
            dto.setText("question" + i);

            result.add(dto);
        }

        return result;
    }
}
