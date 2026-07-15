package com.example.OzonHelper.dto.request.fbo;

import com.example.OzonHelper.dto.response.fbo.TimeSlotDto;
import com.example.OzonHelper.enums.SupplyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSupplyRequest {
    @JsonProperty("draft_id")
    private long draftId;
    @JsonProperty("selected_cluster_warehouses")
    private List<ClusterAndWarehouseInfoDto> clusterAndWarehouseInfoDto;
    @JsonProperty("timeslot")
    private TimeSlotDto timeslot;
    @JsonProperty("supply_type")
    private SupplyType supplyType;


    @Data
    @AllArgsConstructor
    public static class TimeSlotDto {
        @JsonProperty("from_in_timezone")
        private String from;
        @JsonProperty("to_in_timezone")
        private String to;
    }
}
