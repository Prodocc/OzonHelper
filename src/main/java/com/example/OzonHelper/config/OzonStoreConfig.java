package com.example.OzonHelper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@Data
@NoArgsConstructor
public class OzonStoreConfig {
    private String name;
    private String clientId;
    private String apiKey;
    private Map<String, String> chatNameIdMap;
    private String reportFolder;
}
