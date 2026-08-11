package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.SupplyState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplyOrder {
    private LocalDateTime createdDate;
    private long orderId;
    private String orderNumber;
    private String supplyId;
    private SupplyOrderComposition composition;
    private SupplyState state;
    private String bundle_id;
    private String clusterName;
}
