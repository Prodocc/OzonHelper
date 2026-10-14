package com.example.OzonHelper.dto.agent;

import java.util.List;

public record FbsShopCheckResult(
        boolean hasOrders,
        List<String> shops
) {
}
