package com.example.OzonHelper.dto.response.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryDetailsDto {
    @JsonProperty("driver_name")
    private String driverName;
    @JsonProperty("driver_phone")
    private String driverPhone;
    @JsonProperty("vehicle_model")
    private String vehicleModel;
    @JsonProperty("vehicle_number")
    private String vehicleNumber;
}
