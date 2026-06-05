package com.example.OzonHelper.config;

import com.example.OzonHelper.domain.Warehouse;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "dictionaries")
public class WarehouseDictionary {
    private List<Warehouse> warehouses = new ArrayList<>();

    private final Map<Long, Warehouse> lookupMap = new HashMap<>();

    @PostConstruct
    public void initLookupMap() {
        for (Warehouse w : warehouses) {
            lookupMap.put(w.getId(), w);
        }

        System.out.println("Справочник складов загружен. Количество: " + lookupMap.size());
    }

    public Warehouse getById(long warehouseId) {
        return lookupMap.get(warehouseId);
    }
}
