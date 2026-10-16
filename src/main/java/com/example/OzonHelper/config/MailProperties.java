package com.example.OzonHelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "app.mail")
@Data
public class MailProperties {
    private Map<String, String> recipients = new HashMap<>();
}
