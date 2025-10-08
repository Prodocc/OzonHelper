package com.example.OzonHelper.service;

import com.example.OzonHelper.client.MarketplaceClient;
import com.example.OzonHelper.client.OzonClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OzonService {
    private final List<MarketplaceClient> client;

}
