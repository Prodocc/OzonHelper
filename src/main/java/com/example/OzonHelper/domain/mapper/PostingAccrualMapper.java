package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.PostingAccrual;
import com.example.OzonHelper.dto.report.ozon.PostingAccrualDto;

import java.math.BigDecimal;

public class PostingAccrualMapper {

    public PostingAccrual mapToModel(PostingAccrualDto dto) {
        PostingAccrual model = new PostingAccrual();
        model.setSupplyId(dto.getSupplyId());
        model.setType(dto.getType());
        model.setAmount(dto.getAmount());
        model.setSum(parseMoney(dto.getSum()));

        return model;
    }

    private BigDecimal parseMoney(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        String normalized = value
                .replace(" ", "")
                .replace("₽", "")
                .replace(",", ".")
                .replace("\u00A0","")
                .replace("\u202F","")
                .trim();

        return new BigDecimal(normalized);
    }
}
