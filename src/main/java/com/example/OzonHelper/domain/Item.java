package com.example.OzonHelper.domain;

import lombok.Data;

@Data
public class Item {
    private String sku;
    private String name;
    private String article;
    private int quantity;
    private String barcode;
}
