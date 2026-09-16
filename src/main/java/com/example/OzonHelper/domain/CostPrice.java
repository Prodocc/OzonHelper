package com.example.OzonHelper.domain;

import java.math.BigDecimal;

public record CostPrice(
        String article,
        String aliases,
        BigDecimal costPrice) {

}
