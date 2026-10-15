package com.example.OzonHelper.config;

import com.example.OzonHelper.client.MaxClient;
import com.example.OzonHelper.client.OzonClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties({StoreProperties.class})
@EnableScheduling
@EnableAsync
public class AppConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    @Bean
    public SSLContext maxSslContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream inputStream = Files.newInputStream(
                Path.of("config/max-truststore.p12")
        )) {
            keyStore.load(inputStream, "changeit".toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                );

        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(
                null,
                trustManagerFactory.getTrustManagers(),
                null
        );
        return sslContext;
    }

    @Bean
    @Qualifier("defaultHttpClient")
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @Bean
    @Qualifier("maxHttpClient")
    public HttpClient maxHttpClient(SSLContext maxSslContext) {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .sslContext(maxSslContext)
                .build();
    }

    @Bean
    public MaxClient maxClient(MaxBotConfig maxBotConfig, @Value("${max.api.host}") String maxApiHost,
                               @Qualifier("maxHttpClient") HttpClient httpClient, RestClient restClient, ObjectMapper mapper) {
        return new MaxClient(maxBotConfig, maxApiHost, httpClient, restClient, mapper);
    }

    @Bean
    @Qualifier("maxRestClient")
    public RestClient maxRestClient(
            @Qualifier("maxHttpClient") HttpClient maxHttpClient) {
        JdkClientHttpRequestFactory requestFactory
                = new JdkClientHttpRequestFactory(maxHttpClient);

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    @Bean
    public Map<String, OzonClient> ozonClients(StoreProperties storeProperties, @Value("${ozon.api.host}") String ozonApiHost,
                                               @Qualifier("defaultHttpClient") HttpClient httpClient, ObjectMapper objectMapper) {

        List<OzonStoreConfig> ozonStores = storeProperties.getOzon();

        return ozonStores.stream().
                collect(Collectors.toMap(
                        OzonStoreConfig::getClientId,
                        ozonStoreConfig -> new OzonClient(
                                ozonStoreConfig,
                                ozonApiHost,
                                httpClient,
                                objectMapper
                        )
                ));
    }

}
