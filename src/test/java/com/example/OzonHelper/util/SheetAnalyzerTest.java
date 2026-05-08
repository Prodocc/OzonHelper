package com.example.OzonHelper.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.OzonHelper.util.SheetTestDataFactory.createTableWithDotsData;
import static org.assertj.core.api.Assertions.assertThat;

class SheetAnalyzerTest {
    private SheetAnalyzer analyzer;

    @BeforeEach
    public void init() {
        this.analyzer = new SheetAnalyzer();
    }

    @Test
    void findLastNonEmptyRowIndex_WhenDataAtStart() {
        List<List<Object>> table = createTableWithDotsData(3, 4);

        table.get(0).set(0, "some text");

        int lastNonEmptyRowIndex = analyzer.findLastNonEmptyRowIndex(table, 3);

        assertThat(lastNonEmptyRowIndex).isEqualTo(0);
    }

    @Test
    void findLastNonEmptyRowIndex_WhenDataAtEnd() {
        List<List<Object>> table = createTableWithDotsData(3, 4);

        table.get(2).set(0, "some text");

        int lastNonEmptyRowIndex = analyzer.findLastNonEmptyRowIndex(table, 3);

        assertThat(lastNonEmptyRowIndex).isEqualTo(2);
    }

    @Test
    void findLastNonEmptyRowIndex_WhenThereIsNoUsefulData() {
        List<List<Object>> table = createTableWithDotsData(3, 4);

        int lastNonEmptyRowIndex = analyzer.findLastNonEmptyRowIndex(table, 3);

        assertThat(lastNonEmptyRowIndex).isEqualTo(-1);
    }

    @Test
    void findLastNonEmptyRowIndex_shouldHandleNullInput() {
        int lastNonEmptyRowIndex = analyzer.findLastNonEmptyRowIndex(null, 1);

        assertThat(lastNonEmptyRowIndex).isEqualTo(-1);
    }

    @Test
    public void findDateRowIndex_WhenDateAtStart() {
        List<List<Object>> table = createTableWithDotsData(5, 5);
        String targetDay = "04.03.2026";
        int dateColumnIndex = 0;

        table.get(0).set(dateColumnIndex, targetDay);

        int dateRowIndex = analyzer.findDateRowIndex(table, targetDay, dateColumnIndex);

        assertThat(dateRowIndex).isEqualTo(0);
    }

    @Test
    public void findDateRowIndex_WhenDateAtMiddle() {
        List<List<Object>> table = createTableWithDotsData(5, 5);
        String targetDay = "04.03.2026";
        int dateColumnIndex = 0;

        table.get(3).set(dateColumnIndex, targetDay);

        int dateRowIndex = analyzer.findDateRowIndex(table, targetDay, dateColumnIndex);

        assertThat(dateRowIndex).isEqualTo(3);
    }

    @Test
    public void findDateRowIndex_WhenDateAtEnd() {
        List<List<Object>> table = createTableWithDotsData(5, 5);
        String targetDay = "04.03.2026";
        int dateColumnIndex = 0;

        table.get(4).set(dateColumnIndex, targetDay);

        int dateRowIndex = analyzer.findDateRowIndex(table, targetDay, dateColumnIndex);

        assertThat(dateRowIndex).isEqualTo(4);
    }

    @Test
    public void findDateRowIndex_WhenRowIsShorterThanTargetColumnIndex() {
        List<List<Object>> table = createTableWithDotsData(5, 2);
        String targetDay = "04.03.2026";
        int dateColumnIndex = 4;

        int dateRowIndex = analyzer.findDateRowIndex(table, targetDay, dateColumnIndex);

        assertThat(dateRowIndex).isEqualTo(-1);
    }

    @Test
    public void findDateRowIndex_WhenNoDate() {
        List<List<Object>> table = createTableWithDotsData(5, 5);
        String targetDay = "04.03.2026";
        int dateColumnIndex = 0;

        int dateRowIndex = analyzer.findDateRowIndex(table, targetDay, dateColumnIndex);

        assertThat(dateRowIndex).isEqualTo(-1);
    }


    @Test
    public void findDateRowIndex_ShouldHandleNullInput() {
        int dateRowIndex = analyzer.findDateRowIndex(null, "03.04.2026", 0);

        assertThat(dateRowIndex).isEqualTo(-1);
    }

}