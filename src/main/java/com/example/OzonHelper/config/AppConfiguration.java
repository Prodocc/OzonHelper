package com.example.OzonHelper.config;

import com.example.OzonHelper.client.OzonClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;
import java.util.List;

@Configuration
@EnableConfigurationProperties({StoreProperties.class})
@EnableScheduling
@EnableAsync
public class AppConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    @Bean
    public List<OzonClient> ozonClient(StoreProperties storeProperties, @Value("${ozon.api.host}") String ozonApiHost, HttpClient httpClient, ObjectMapper objectMapper) {

        List<OzonStoreConfig> ozonStores = storeProperties.getOzon();

        return ozonStores.stream().map(
                ozonStoreConfig -> new OzonClient(
                        ozonStoreConfig,
                        ozonApiHost,
                        httpClient,
                        objectMapper
                )).toList();
    }

}
