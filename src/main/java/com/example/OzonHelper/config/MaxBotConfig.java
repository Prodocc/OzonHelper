package com.example.OzonHelper.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "bot.max")
public class MaxBotConfig {
    private String name;
    private String token;
}
