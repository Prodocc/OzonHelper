package com.example.OzonHelper.dto.response.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTags {
    @JsonProperty("is_super_fbo")
    private boolean isSuperFbo;
}
