package com.example.OzonHelper.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
public class OzonStoreConfig {
    private String name;
    private String clientId;
    private String apiKey;
    private String legalEntity;
    private long fbsWarehouseId;
    private Map<String, String> chatNameIdMap;
}
