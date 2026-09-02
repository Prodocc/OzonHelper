package com.example.OzonHelper.client;

import com.example.OzonHelper.config.MaxBotConfig;
import com.example.OzonHelper.dto.request.max.*;
import com.example.OzonHelper.dto.response.max.*;
import com.example.OzonHelper.enums.max.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Data
public class MaxClient {
    private static final String WEBHOOK_URL = "https://profiles-alberta-keen-that.trycloudflare.com/webhook";
    private final String SECRET = "test-secret";

    private final String name;
    private final String token;
    private final String apiHost;
    private final HttpClient httpClient;
    private final RestClient restClient;
    private final ObjectMapper mapper;

    public MaxClient(MaxBotConfig config, String maxApiHost, HttpClient httpClient, RestClient restClient, ObjectMapper objectMapper) {
        this.name = config.getName();
        this.token = config.getToken();
        this.apiHost = maxApiHost;
        this.httpClient = httpClient;
        this.restClient = restClient;
        this.mapper = objectMapper;
    }

    private HttpResponse<String> createJsonBodyAndSendPostRequest(String url, Object JsonRequestBodyObject) throws IOException, InterruptedException {
        String requestJsonBody = mapper.writeValueAsString(JsonRequestBodyObject);
        System.out.println(requestJsonBody);
        return sendPostRequest(url, requestJsonBody);
    }

    private HttpResponse<String> createJsonBodyAndSendGetRequest(String url, Object JsonRequestBodyObject) throws IOException, InterruptedException {
        String requestJsonBody = mapper.writeValueAsString(JsonRequestBodyObject);
        return sendGetRequest(url, requestJsonBody);
    }

    private HttpResponse<String> sendEmptyPostRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);

        return response;
    }

    private HttpResponse<String> sendGetRequest(String url, String requestBodyJson) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);

        return response;
    }

    private HttpResponse<String> sendPostRequest(String url, String requestBodyJson) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);

        return response;
    }

    private String sendMediaPostRequest(
            String url,
            Path filePath) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add(
                "data",
                new FileSystemResource(filePath)
        );

        return restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    public GetBotInfoResponse getBotInfo() throws IOException, InterruptedException {
        HttpResponse<String> response = createJsonBodyAndSendGetRequest(
                MaxApiEndpoint.BOT_INFO.getFullUrl(apiHost),
                HttpRequest.BodyPublishers.noBody()
        );

        return mapper.readValue(response.body(), GetBotInfoResponse.class);
    }

    public PostWebHookSubscriptionResponse sendWebHookSubscriptions() throws IOException, InterruptedException {
        PostWebHookSubscriptionRequest request = new PostWebHookSubscriptionRequest();

        request.setUrl(WEBHOOK_URL);
        request.setUpdateTypes(Collections.singletonList(UpdateType.MESSAGE_CREATED));
        request.setSecret(SECRET);

        HttpResponse<String> response = createJsonBodyAndSendPostRequest(
                MaxApiEndpoint.SUBSCRIPTIONS.getFullUrl(apiHost),
                request
        );

        return mapper.readValue(response.body(), PostWebHookSubscriptionResponse.class);
    }

    private GetUploadURLResponse getUploadImageUrl() throws IOException, InterruptedException {
        HttpResponse<String> response = sendEmptyPostRequest(
                MaxApiEndpoint.UPLOADS.getFullUrl(apiHost)
                        + "?type=" + UploadType.IMAGE.getApiValue()
        );

        return mapper.readValue(response.body(), GetUploadURLResponse.class);
    }

    private UploadImageResponse uploadImage(Path filePath) throws IOException, InterruptedException {
        GetUploadURLResponse uploadUrl = getUploadImageUrl();

        String responseBody = sendMediaPostRequest(
                uploadUrl.getUrl(),
                filePath
        );

        return mapper.readValue(responseBody, UploadImageResponse.class);
    }

    public void sendImage(String chatId, String text, Path filePath) throws IOException, InterruptedException {
        UploadImageResponse uploadImageResponse = uploadImage(filePath);

        SendImageRequest request = new SendImageRequest();
        ImageAttachmentDto attachment = new ImageAttachmentDto();
        ImageAttachmentPayloadDto payload = new ImageAttachmentPayloadDto();
        payload.setPhotos(uploadImageResponse.getPhotos());

        attachment.setType(AttachmentType.IMAGE);
        attachment.setPayload(payload);

        request.setAttachments(Collections.singletonList(attachment));
        request.setText(text);
        request.setNotify(true);

        createJsonBodyAndSendPostRequest(
                MaxApiEndpoint.MESSAGES.getFullUrl(apiHost) + "?chat_id=" + chatId,
                request
        );
    }

    public void addButton(String chatId, String label, ButtonType type, String callbackData) throws IOException, InterruptedException {
        AddButtonRequest request = new AddButtonRequest();
        ButtonAttachmentDto attachment = new ButtonAttachmentDto();
        ButtonAttachmentPayloadDto payload = new ButtonAttachmentPayloadDto();

        ButtonDto button = new ButtonDto();
        button.setText(label);
        button.setType(type);
        button.setPayload(callbackData);

        payload.setButtons(List.of(Collections.singletonList(button)));
        attachment.setType(AttachmentType.KEYBOARD);
        attachment.setPayload(payload);
        request.setAttachments(List.of(attachment));
        request.setText("Test button");

        createJsonBodyAndSendPostRequest(
                MaxApiEndpoint.MESSAGES.getFullUrl(apiHost) + "?chat_id=" + chatId,
                request
        );
    }

    private void checkResponse(HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    "MAX API error" +
                            response.statusCode() +
                            ", body: " +
                            response.body()
            );
        }
    }
}
