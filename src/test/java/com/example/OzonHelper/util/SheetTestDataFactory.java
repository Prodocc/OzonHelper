package com.example.OzonHelper.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public static String[] getStartAndEndDateForTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);
        LocalDate nextDate = LocalDate.now();
        if (nextDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextDate = nextDate.plusDays(3);
        } else {
            nextDate = nextDate.plusDays(1);
        }
        String nextDay = nextDate.format(formatter);

        return new String[]{today, nextDay};
    }
}
