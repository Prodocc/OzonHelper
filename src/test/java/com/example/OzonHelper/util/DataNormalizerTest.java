package com.example.OzonHelper.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.OzonHelper.util.SheetTestDataFactory.createTable;
import static org.assertj.core.api.Assertions.assertThat;


class DataNormalizerTest {
    private DataNormalizer normalizer;

    @BeforeEach
    public void init() {
        this.normalizer = new DataNormalizer();
    }

    @Test
    void normalize_ShouldAddEmptyStrings_WhenSizeIsLess() {
        List<List<Object>> table = createTable(3, 4);
        int targetSize = 6;

        assertThat(table.get(0)).hasSizeLessThan(targetSize);

        normalizer.normalizeData(table, targetSize);

        for (List<Object> row : table) {
            assertThat(row).hasSize(targetSize);
        }
    }

    @Test
    void normalize_ShouldDoNothing_WhenSizeIsEqual() {
        List<List<Object>> table = createTable(3, 6);
        int targetSize = 6;

        assertThat(table.get(0)).hasSize(targetSize);

        normalizer.normalizeData(table, targetSize);

        for (List<Object> row : table) {
            assertThat(row).hasSize(targetSize);
        }
    }

    @Test
    void normalize_ShouldDoNothing_WhenSizeIsGreater() {
        List<List<Object>> table = createTable(3, 10);
        int actualSize = 10;
        int targetSize = 6;

        assertThat(table.get(0)).hasSize(actualSize);

        normalizer.normalizeData(table, targetSize);

        for (List<Object> row : table) {
            assertThat(row).hasSize(actualSize);
        }
    }

    @Test
    void normalize_ShouldHandleEmptyInput() {
        List<List<Object>> table = createTable(0, 0);
        int targetSize = 5;

        assertThat(table).isEmpty();

        normalizer.normalizeData(table, targetSize);
    }

    @Test
    void normalize_ShouldHandleNullInput() {
        int targetSize = 5;

        normalizer.normalizeData(null, targetSize);
    }
}