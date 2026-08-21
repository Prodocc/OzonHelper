package com.example.OzonHelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String sku;
    private String name;
    private String article;
    private int quantity;
    private String barcode;
}
