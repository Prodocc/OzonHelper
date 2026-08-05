package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto {
    @JsonProperty("sku")
    private String sku;
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
    /**
     * Идентификатор товара в системе продавца (артикул).
     * В реальном API приходит как "offer_id", несмотря на то, что в документации
     * указан "contractor_item_code".
     */
    @JsonProperty("offer_id")
    private String article;
    @JsonProperty("shipment_type")
    private String shipmentType;
    @JsonProperty("placement_zone")
    private String placementZone;

    @Override
    public String toString() {
        return "SupplyOrderItem{" +
                "sku=" + sku +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", barcode='" + barcode + '\'' +
                ", productId=" + productId +
                ", quant=" + quant +
                ", volume=" + volume +
                ", totalVolume=" + totalVolume +
                ", article='" + article + '\'' +
                ", shipmentType='" + shipmentType + '\'' +
                ", placementZone='" + placementZone + '\'' +
                '}';
    }
}
