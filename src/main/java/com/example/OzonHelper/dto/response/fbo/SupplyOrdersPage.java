package com.example.OzonHelper.dto.response.fbo;

import lombok.Data;

import java.util.List;

public record SupplyOrdersPage(
        List<String> orderIds,
        String nextCursor) {

}


