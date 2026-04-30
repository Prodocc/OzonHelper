package com.example.OzonHelper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "api.google")
@Getter
@Setter
public class GoogleSheetsProperties {

    private Map<String, String> sheets = new HashMap<>();

}
