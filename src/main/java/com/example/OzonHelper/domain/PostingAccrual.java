package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.AccrualType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PostingAccrual {
    private String supplyId;
    private LocalDate accrualDate;
    private AccrualType type;
    private String article;
    private long sku;
    private String productName;
    private int amount;
    private BigDecimal sellerPrice;
    private LocalDate acceptanceDate;
    private BigDecimal sum;
}
