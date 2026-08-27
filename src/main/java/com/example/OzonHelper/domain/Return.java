package com.example.OzonHelper.domain;

import lombok.Data;

@Data
public class Return {
    private long id;
    private String reason;
    private Warehouse warehouse;
    private String postingNumber;
    private Product product;
    private String labelBarcode;
}
