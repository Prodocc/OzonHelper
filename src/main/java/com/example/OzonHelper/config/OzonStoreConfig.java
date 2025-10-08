package com.example.OzonHelper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
public class OzonStoreConfig {
    private String name;
    private String clientId;
    private String apiKey;
    private String[] skus;
}
