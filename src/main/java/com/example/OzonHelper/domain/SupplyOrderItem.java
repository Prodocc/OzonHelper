package com.example.OzonHelper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplyOrderItem {
    @JsonProperty("sku")
    private long sku;
    @JsonProperty("name")
    private String name;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("barcode")
    private String barcode;
    @JsonProperty("product_id")
    private long productId;
    @JsonProperty("quant")
    private int quant;
    @JsonProperty("volume_in_litres")
    private double volume;
    @JsonProperty("total_volume_in_litres")
    private double totalVolume;
    @JsonProperty("contractor_item_code")
    private String article;
    @JsonProperty("shipment_type")
    private String shipmentType;
    @JsonProperty("placement_zone")
    private String placementZone;
}
