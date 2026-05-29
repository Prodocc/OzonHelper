package com.example.OzonHelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "dictionaries")
@Component
public class WarehouseProperties {

    List<WarehouseConfig> warehouses = new ArrayList<>();
}
