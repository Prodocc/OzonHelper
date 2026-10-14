package com.example.OzonHelper.service;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.agent.FbsShopCheckResult;
import com.example.OzonHelper.dto.response.seller.SubscriptionDto;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class OzonAgentTools {
    private final Map<String, OzonClient> clients;
    private final FbsService fbsService;

    public OzonAgentTools(Map<String, OzonClient> clients, FbsService fbsService){
        this.clients = clients;
        this.fbsService = fbsService;
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

    @Tool(description = "Возвращает названия магазинов в которых есть заказы по схеме fbs в статусе - ожидает сборки")
    public FbsShopCheckResult getShopNameWithAwaitingPackage() {
        List<String> shops = fbsService.getShopsWithAwaitingPackaging();

        return new FbsShopCheckResult(
                !shops.isEmpty(),
                shops
        );
    }

    @Tool(description = "Возвращает названия магазинов в которых есть заказы по схеме fbs в статусе - ожидает отгрузки")
    public FbsShopCheckResult getShopNameWithAwaitingDeliver() {
        List<String> shops = fbsService.getShopsWithAwaitingDeliver();

        return new FbsShopCheckResult(
                !shops.isEmpty(),
                shops
        );
    }
}
