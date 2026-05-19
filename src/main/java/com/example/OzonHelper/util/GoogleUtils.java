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
}
