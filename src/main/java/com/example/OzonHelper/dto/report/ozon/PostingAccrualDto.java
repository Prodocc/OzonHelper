package com.example.OzonHelper.dto.report.ozon;

import com.example.OzonHelper.enums.AccrualType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Optional;

@Data
public class PostingAccrualDto {
    private String supplyId;
    private String sum;
    private int amount;
    private AccrualType type;
}
