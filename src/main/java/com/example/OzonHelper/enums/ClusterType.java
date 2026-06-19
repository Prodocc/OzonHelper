package com.example.OzonHelper.enums;

import lombok.Getter;

public enum ClusterType {
    CLUSTER_TYPE_OZON("CLUSTER_TYPE_OZON", "Кластер в России");

    private final String apiValue;
    @Getter
    private final String description;

    ClusterType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
