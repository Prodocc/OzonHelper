package com.example.OzonHelper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "reports.crossdock")
public record CrossdockReportProperties(Path root) {

}
