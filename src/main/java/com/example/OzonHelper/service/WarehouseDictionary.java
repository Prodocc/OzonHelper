package com.example.OzonHelper.service;

import com.example.OzonHelper.config.WarehouseConfig;
import com.example.OzonHelper.config.WarehouseProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarehouseDictionary {

    public final Map<Long, WarehouseConfig> warehouses = new HashMap<>();

    public WarehouseDictionary(WarehouseProperties properties) {
        List<WarehouseConfig> configs = properties.getWarehouses();

        for (WarehouseConfig cfg : configs) {
            warehouses.put(cfg.getId(), cfg);
        }
    }

    public WarehouseConfig getById(long id) {
        return warehouses.get(id);
    }
}
