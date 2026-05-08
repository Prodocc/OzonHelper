package com.example.OzonHelper.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataNormalizer {

    public void normalizeData(List<List<Object>> sheetData, int targetSize) {
        if (sheetData == null) {
            return;
        }
        for (List<Object> row : sheetData) {
            if (row.size() < targetSize) {
                int size = row.size();
                for (int i = 0; i < targetSize - size; i++) {
                    row.add("");
                }
            }
        }
    }
}
