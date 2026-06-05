package com.example.OzonHelper.domain;

import lombok.Data;

import java.util.Optional;

@Data
public class Warehouse {
    private long id;
    private String name;
    private String address;
    private long clusterId;


}
