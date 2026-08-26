package com.example.OzonHelper.dto.response.returns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReturnDto {
    @JsonProperty("id")
    private long id;
}
