package com.example.OzonHelper.service.report.crossdock;

import com.example.OzonHelper.domain.Item;
import com.example.OzonHelper.domain.PostingAccrual;
import com.example.OzonHelper.domain.Supply;
import com.example.OzonHelper.domain.SupplyOrderComposition;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CrossDockDataBuilderTest {
    private final CrossDockDataBuilder dataBuilder = new CrossDockDataBuilder();

    @Test
    public void buildCrossDockData_singleItem_buildCorrectRow() {
        String shopName = "shop1";

        String supplyId = "1";

        Map<String, PostingAccrual> input = new HashMap<>();

        PostingAccrual pa1 = new PostingAccrual();

        Supply supply1 = new Supply();
        SupplyOrderComposition comp1 = new SupplyOrderComposition();
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("sku1", "name1", "article1", 10, "barcode1");

        items.add(item1);
        comp1.setItems(items);

        supply1.setClusterName("cluster1");
        supply1.setComposition(comp1);

        pa1.setSupplyId("1");
        pa1.setSupply(supply1);
        pa1.setSum(new BigDecimal("-400.00"));

        input.put(supplyId, pa1);

        List<List<Object>> actual = dataBuilder.buildCrossDockData(shopName, input);

        List<List<Object>> expected = new ArrayList<>();
        List<Object> row = List.of(
                shopName,
                pa1.getSupplyId(),
                pa1.getSupply().getClusterName(),
                item1.getSku(),
                item1.getArticle(),
                item1.getQuantity(),
                pa1.getSum(),
                new BigDecimal("-40.00")
        );

        expected.add(row);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void buildCrossDockData_twoItems_PositiveSum_buildCorrectRows() {
        String shopName = "shop1";

        String supplyId = "1";

        Map<String, PostingAccrual> input = new HashMap<>();

        PostingAccrual pa1 = new PostingAccrual();

        Supply supply1 = new Supply();
        SupplyOrderComposition comp1 = new SupplyOrderComposition();
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("sku1", "name1", "article1", 10, "barcode1");
        Item item2 = new Item("sku2", "name2", "article2", 25, "barcode2");

        items.add(item1);
        items.add(item2);
        comp1.setItems(items);

        supply1.setClusterName("cluster1");
        supply1.setComposition(comp1);

        pa1.setSupplyId("1");
        pa1.setSupply(supply1);
        pa1.setSum(new BigDecimal("400.00"));

        input.put(supplyId, pa1);

        List<List<Object>> actual = dataBuilder.buildCrossDockData(shopName, input);

        List<List<Object>> expected = new ArrayList<>();
        List<Object> row1 = List.of(
                shopName,
                pa1.getSupplyId(),
                pa1.getSupply().getClusterName(),
                item1.getSku(),
                item1.getArticle(),
                item1.getQuantity(),
                pa1.getSum(),
                new BigDecimal("11.42")
        );

        List<Object> row2 = List.of(
                shopName,
                pa1.getSupplyId(),
                pa1.getSupply().getClusterName(),
                item2.getSku(),
                item2.getArticle(),
                item2.getQuantity(),
                pa1.getSum(),
                new BigDecimal("11.42")
        );

        expected.add(row1);
        expected.add(row2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void buildCrossDockData_twoItems_NegativeSum_buildCorrectRows() {
        String shopName = "shop1";

        String supplyId = "1";

        Map<String, PostingAccrual> input = new HashMap<>();

        PostingAccrual pa1 = new PostingAccrual();

        Supply supply1 = new Supply();
        SupplyOrderComposition comp1 = new SupplyOrderComposition();
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("sku1", "name1", "article1", 10, "barcode1");
        Item item2 = new Item("sku2", "name2", "article2", 25, "barcode2");

        items.add(item1);
        items.add(item2);
        comp1.setItems(items);

        supply1.setClusterName("cluster1");
        supply1.setComposition(comp1);

        pa1.setSupplyId("1");
        pa1.setSupply(supply1);
        pa1.setSum(new BigDecimal("-400.00"));

        input.put(supplyId, pa1);

        List<List<Object>> actual = dataBuilder.buildCrossDockData(shopName, input);

        List<List<Object>> expected = new ArrayList<>();
        List<Object> row1 = List.of(
                shopName,
                pa1.getSupplyId(),
                pa1.getSupply().getClusterName(),
                item1.getSku(),
                item1.getArticle(),
                item1.getQuantity(),
                pa1.getSum(),
                new BigDecimal("-11.43")
        );

        List<Object> row2 = List.of(
                shopName,
                pa1.getSupplyId(),
                pa1.getSupply().getClusterName(),
                item2.getSku(),
                item2.getArticle(),
                item2.getQuantity(),
                pa1.getSum(),
                new BigDecimal("-11.43")
        );

        expected.add(row1);
        expected.add(row2);

        assertThat(actual).isEqualTo(expected);
    }
}
