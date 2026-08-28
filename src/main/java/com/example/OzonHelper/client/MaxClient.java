package com.example.OzonHelper.client;

import com.example.OzonHelper.config.MaxBotConfig;
import com.example.OzonHelper.dto.response.max.GetBotInfoResponse;
import com.example.OzonHelper.enums.MaxApiEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Data
public class MaxClient {
    private final String name;
    private final String token;
    private final String apiHost;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public MaxClient(MaxBotConfig config, String maxApiHost, HttpClient httpClient, ObjectMapper objectMapper) {
        this.name = config.getName();
        this.token = config.getToken();
        this.apiHost = maxApiHost;
        this.httpClient = httpClient;
        this.mapper = objectMapper;
    }

    private HttpResponse<String> createJsonBodyAndSendPostRequest(String url, Object JsonRequestBodyObject) throws IOException, InterruptedException {
        String requestJsonBody = mapper.writeValueAsString(JsonRequestBodyObject);
        return sendRequest(url, requestJsonBody);
    }

    private HttpResponse<String> createJsonBodyAndSendGetRequest(String url, Object JsonRequestBodyObject) throws IOException, InterruptedException {
        String requestJsonBody = mapper.writeValueAsString(JsonRequestBodyObject);
        return sendGetRequest(url, requestJsonBody);
    }

    private HttpResponse<String> sendGetRequest(String url, String requestBodyJson) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendRequest(String url, String requestBodyJson) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public GetBotInfoResponse getBotInfo() throws IOException, InterruptedException {
        HttpResponse<String> response = createJsonBodyAndSendGetRequest(
                MaxApiEndpoint.BOT_INFO.getFullUrl(apiHost),
                HttpRequest.BodyPublishers.noBody()
        );

        System.out.println(response.body());

        return mapper.readValue(response.body(), GetBotInfoResponse.class);
    }
}
