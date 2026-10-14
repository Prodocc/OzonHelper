package com.example.OzonHelper.service;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.seller.SubscriptionDto;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OzonAgentTools {
    private final Map<String, OzonClient> clients;

    public OzonAgentTools(Map<String, OzonClient> clients){
        this.clients = clients;
    }

    @Tool(description = "Получает информацию о подписке Ozon для указанного магазина")
    public SubscriptionDto getSubscriptionInfo(
            @ToolParam(description = "Название магазина")
            String shopName
    ){

        OzonClient client = clients.values().stream()
                .filter(c -> c.getShopName().equalsIgnoreCase(shopName))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("магазин не найден: " + shopName)
        );


        try {
            SubscriptionDto result = client.getSubscriptionInfo();

            return result;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
