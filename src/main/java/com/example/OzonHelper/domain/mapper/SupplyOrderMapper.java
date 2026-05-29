package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.Cluster;
import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.dto.response.supply.ClusterDto;
import com.example.OzonHelper.dto.response.supply.GetClustersResponse;

import java.util.List;

public class SupplyOrderMapper {

    public Cluster mapToDomain(ClusterDto clusterDto) {
        Cluster cluster = new Cluster();

        cluster.setId(clusterDto.getId());
        cluster.setName(clusterDto.getName());
        cluster.setMacrolocalClusterId(clusterDto.getMacrolocalClusterId());

        return cluster;
    }

    public Warehouse mapToDomain(GetClustersResponse.WarehouseDto warehouseDto){
        Warehouse warehouse = new Warehouse();

        warehouse.setId(warehouseDto.getId());

        return warehouse;
    }
}
