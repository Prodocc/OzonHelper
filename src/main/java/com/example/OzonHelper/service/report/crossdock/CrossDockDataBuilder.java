package com.example.OzonHelper.service.report.crossdock;

import com.example.OzonHelper.domain.Item;
import com.example.OzonHelper.domain.PostingAccrual;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CrossDockDataBuilder {

    public List<List<Object>> buildCrossDockData(String shopName, Map<String, PostingAccrual> accrualsBySupplyId) {
        List<List<Object>> result = new ArrayList<>();

        for (PostingAccrual accrual : accrualsBySupplyId.values()) {
            int totalItemsQuantity = accrual.getSupply().getComposition().getItems().stream().mapToInt(Item::getQuantity).sum();
            BigDecimal sum = accrual.getSum();
            BigDecimal perItem = sum.divide(new BigDecimal(totalItemsQuantity), RoundingMode.FLOOR);
            for (Item item : accrual.getSupply().getComposition().getItems()) {
                List<Object> row = List.of(
                        shopName,
                        accrual.getSupplyId(),
                        accrual.getSupply().getClusterName(),
                        item.getSku(),
                        item.getArticle(),
                        item.getQuantity(),
                        accrual.getSum(),
                        perItem
                );
                result.add(row);
            }
        }
        return result;
    }
}
