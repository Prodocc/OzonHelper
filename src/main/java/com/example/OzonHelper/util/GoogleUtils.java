package com.example.OzonHelper.util;

import com.google.api.services.sheets.v4.model.GridRange;

public class GoogleUtils {

    public static String buildRange(String title, String columnStart, String columnEnd, int scopeStartRow) {
        int tableScopeStartRow = scopeStartRow + 1;
        return title + "!" + columnStart + tableScopeStartRow + ":" + columnEnd + tableScopeStartRow;
    }

    public static GridRange createRowRange(int rowStart, int rowEnd) {
        GridRange gridRange = new GridRange();
        gridRange.setStartRowIndex(rowStart);
        gridRange.setEndRowIndex(rowEnd);
        return gridRange;
    }

    public static String colIndexToLetter(int index) {
        StringBuilder sb = new StringBuilder();
        index++; // 1-based для конвертации
        while (index > 0) {
            index--;
            int remainder = index % 26;
            sb.insert(0, (char) ('A' + remainder));
            index /= 26;
        }
        return sb.toString();
    }
}
