package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.StockItem;
import com.example.OzonHelper.dto.response.fbo.StockDto;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public StockItem mapToModel(StockDto dto) {
        StockItem item = new StockItem();

        item.setSku(dto.getSku());
        item.setArticle(dto.getArticle());
        item.setAvailableStock(dto.getAvailableStock() + dto.getValidStock());
        item.setInTransitStock(dto.getInSupplyStock() + dto.getInTransitStock());

        return item;
    }
}
