package com.example.OzonHelper.domain;

import lombok.Data;

import java.util.List;

@Data
public class SupplyOrderComposition {
    private List<Item> items;
    private int totalCount;
}
