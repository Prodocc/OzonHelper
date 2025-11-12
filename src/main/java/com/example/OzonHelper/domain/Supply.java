package com.example.OzonHelper.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Supply {
    private String orderNumber;
    private Timeslot timeslot;
    private String supplierDetails;
    private Warehouse shippingWarehouse;
    private Warehouse receivingWarehouse;
    private String legalEntityName;
    private Carrier carrier;
    private Vehicle vehicle;

    private void setTimeSlot(LocalDateTime from, LocalDateTime to) {
        this.timeslot.setFrom(from);
        this.timeslot.setTo(to);
    }
}
