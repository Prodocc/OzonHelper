package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.CostPrice;
import com.example.OzonHelper.dto.report.ozon.CostPriceDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CostPriceMapper {
    public CostPrice mapToModel(CostPriceDto dto) {
        String article = dto.getArticle();
        BigDecimal costPrice = PostingAccrualMapper.parseMoney(dto.getCostPrice());

        return new CostPrice(article, "", costPrice);
    }
}
