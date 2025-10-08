package com.example.OzonHelper.dto.csv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "Принят в обработку", "Статус", "OZON id", "Артикул", "Количество"
})
public class OzonPostingRow {

    @JsonProperty("Принят в обработку")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime acceptedAt;

    @JsonProperty("Статус")
    private String status;

    @JsonProperty("OZON id")
    private Long sku;

    @JsonProperty("Артикул")
    private String article;

    @JsonProperty("Количество")
    private int quantity;


}
