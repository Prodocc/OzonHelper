package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.WarehouseType;
import lombok.Data;

import java.util.List;

@Data
public class Cluster {
    private long id;
    private String name;
    private List<Warehouse> warehouses;
    private WarehouseType type;
    private long macrolocalClusterId;
}
