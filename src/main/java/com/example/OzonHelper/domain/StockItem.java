package com.example.OzonHelper.domain;

import lombok.Data;

@Data
public class StockItem {
    private String sku;
    private String article;
    private int availableStock;
    private int inTransitStock;
    private int sellsForYesterday;
    private int sellsForLastWeek;
    private int sellsForLastThreeWeeks;
}
