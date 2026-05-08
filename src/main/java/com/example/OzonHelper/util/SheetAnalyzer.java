package com.example.OzonHelper.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SheetAnalyzer {

    public int findLastNonEmptyRowIndex(List<List<Object>> sheetData, int minUsefulDataLength) {
        if (sheetData == null || minUsefulDataLength < 0) {
            return -1;
        }
        int rowStart = sheetData.size() - 1;
        for (int i = rowStart; i >= 0; i--) {
            var row = sheetData.get(i);
            if (!row.isEmpty() && row.stream().anyMatch(o -> o.toString().trim().length() > minUsefulDataLength)) {
                return i;
            }
        }
        return -1;
    }

    public int findDateRowIndex(List<List<Object>> sheetData, String targetDay, int dateColumnIndex) {
        if (sheetData == null || dateColumnIndex < 0 || targetDay == null) {
            return -1;
        }
        for (int i = 0; i < sheetData.size(); i++) {
            var row = sheetData.get(i);
            if (row != null && row.size() > dateColumnIndex) {
                var cellValue = row.get(dateColumnIndex);
                if (cellValue != null && row.get(dateColumnIndex).toString().equals(targetDay)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
