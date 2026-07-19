package com.example.OzonHelper.dto.request.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetStocksRequest {
    @JsonProperty("skus")
    private List<String> skus;
}
