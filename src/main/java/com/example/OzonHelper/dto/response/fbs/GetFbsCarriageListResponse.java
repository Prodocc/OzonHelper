package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

import java.util.List;

@Data
public class GetFbsCarriageListResponse {
    @JsonProperty("methods")
    private List<CarriageMethodDto> carriageMethodsList;
}
