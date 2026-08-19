package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.Item;
import com.example.OzonHelper.domain.SupplyOrderComposition;
import com.example.OzonHelper.dto.response.fbo.ItemDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderCompositionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class SupplyOrderCompositionMapper {

    public SupplyOrderComposition mapToModel(SupplyOrderCompositionDto dto) {
        SupplyOrderComposition composition = new SupplyOrderComposition();

        List<Item> items = new ArrayList<>();
        for (ItemDto itemDto : dto.items()) {
            items.add(mapToModel(itemDto));
        }

        composition.setItems(items);
        composition.setTotalCount(dto.totalCount());

        return composition;
    }

    private Item mapToModel(ItemDto dto) {
        Item item = new Item();
        item.setSku(dto.getSku());
        item.setName(dto.getName());
        item.setArticle(dto.getArticle());
        item.setQuantity(dto.getQuantity());
        item.setBarcode(dto.getBarcode());

        return item;
    }
}
