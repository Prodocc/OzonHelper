package com.example.OzonHelper.dto.report.ozon;

import com.example.OzonHelper.enums.AccrualType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Optional;

@Data
public class PostingAccrualsDto {
    private String supplyId;
    private String sum;
    private int count;
    private AccrualType type;
}
