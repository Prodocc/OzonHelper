package com.example.OzonHelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private String article;
    private String name;
    private int quantity;
}
