package com.example.OzonHelper.dto.response.fbo;

import lombok.Data;

import java.util.List;


public record SupplyOrderCompositionDto(
        List<ItemDto> items,
        int totalCount) {
}
