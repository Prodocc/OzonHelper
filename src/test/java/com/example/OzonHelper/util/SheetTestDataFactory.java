package com.example.OzonHelper.util;

import java.util.ArrayList;
import java.util.List;

public class SheetTestDataFactory {

    public static List<List<Object>> createTableWithDots(int rows, int cols) {
        List<List<Object>> table = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            table.add(new ArrayList<>());
        }

        for (List<Object> row : table) {
            for (int i = 0; i < cols; i++) {
                row.add(".");
            }
        }

        return table;
    }

    public static List<List<Object>> createTable(int rows, int cols) {
        List<List<Object>> table = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            table.add(new ArrayList<>());
        }

        for (List<Object> row : table) {
            for (int i = 0; i < cols; i++) {
                row.add("");
            }
        }

        return table;
    }

    public static SheetScope createDefaultScope() {
        return new SheetScope(0, 5, 5, true);
    }

    public static SheetScope createScopeWithBounds(int startIndex, int endIndex, int lastIndex) {
        return new SheetScope(startIndex, endIndex, lastIndex, true);
    }
}
