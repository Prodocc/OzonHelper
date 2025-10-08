package com.example.OzonHelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "stores")
public class StoreProperties {

    private List<OzonStoreConfig> ozon;
    private List<WbStoreConfig> wb;
}
