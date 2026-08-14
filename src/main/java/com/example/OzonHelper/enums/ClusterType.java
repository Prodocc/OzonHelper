package com.example.OzonHelper.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ClusterType {
    CLUSTER_TYPE_OZON("OZON", "Кластер в России"),
    CLUSTER_TYPE_CIS("CIS","Кластер в СНГ");

    @JsonValue
    private final String apiValue;
    @Getter
    private final String description;

    ClusterType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
