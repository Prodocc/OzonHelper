package com.example.OzonHelper.domain;

import lombok.Data;

import java.math.BigDecimal;

public record StoredCostPrice(
        String article,
        String aliases,
        BigDecimal costPrice,
        int rowNumber
) {

}
