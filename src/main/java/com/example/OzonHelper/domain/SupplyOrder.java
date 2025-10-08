package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.SupplyStatus;
import lombok.Data;

@Data
public class SupplyOrder {
    private boolean isSuper;
    private SupplyStatus state;
    private long supplyOrderId;
    private String supplyOrderNumber;
    private Warehouse warehouse;
    private Timeslot timeslot;
}
