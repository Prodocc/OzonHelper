package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.Timeslot;
import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.dto.response.supply.TimeSlotDto;
import com.example.OzonHelper.dto.response.supply.WarehouseDto;

public class SupplyOrderMapper {
//    public SupplyOrder toSupplyOrder(SupplyOrderInfoDto supplyInfo, SupplyOrderContentDto supplyContent, OzonClient client) {
//        SupplyOrder supplyOrder = new SupplyOrder();
//
//        supplyOrder.setOrderNumber(supplyInfo.getOrderNumber());
//        supplyOrder.setTimeslot(mapToDomain(supplyInfo.getTimeslotWrapper().getTimeslot()));
//        supplyOrder.setSupplierDetails(client.getSupplierDetails());
//        supplyOrder.setShippingWarehouse(mapToDomain(supplyInfo.getShippingWarehouse()));
//        supplyOrder.setReceivingWarehouse(mapToDomain(supplyInfo.getSupplies().get(0).getReceivingWarehouse()));
////        supplyOrder.setSupplies()
//
//        return supplyOrder;
//    }

    private Timeslot mapToDomain(TimeSlotDto dto) {
        return new Timeslot(dto.getFrom(), dto.getTo());
    }

    private Warehouse mapToDomain(WarehouseDto dto) {
        return new Warehouse(dto.getAddress(), dto.getName(), dto.getId());
    }
}
