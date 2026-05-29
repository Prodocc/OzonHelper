package com.example.OzonHelper.service;

import com.example.OzonHelper.client.OzonClient;
import org.springframework.stereotype.Service;

public class WarehouseEnricher {
    private final OzonClient ozonClient;

    public WarehouseEnricher(OzonClient ozonClient) {
        this.ozonClient = ozonClient;
    }

}
