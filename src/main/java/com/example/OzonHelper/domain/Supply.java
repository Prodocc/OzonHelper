package com.example.OzonHelper.domain;

import com.example.OzonHelper.enums.ozon.SupplyState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Supply {
    private LocalDateTime createdDate;
    private long orderId;
    private String orderNumber;
    private String supplyId;
    private SupplyOrderComposition composition;
    private SupplyState state;
    private String bundleId;
    private String clusterName;
}
