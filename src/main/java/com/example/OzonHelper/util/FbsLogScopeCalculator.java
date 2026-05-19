package com.example.OzonHelper.util;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FbsLogScopeCalculator {
    private final SheetAnalyzer analyzer;

    public FbsLogScopeCalculator(SheetAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    //add check null rule
    public SheetScope calculateScope(List<List<Object>> logList, int rowsToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);
        LocalDate nextDate = LocalDate.now();
        if (nextDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextDate = nextDate.plusDays(3);
        } else {
            nextDate = nextDate.plusDays(1);
        }
        String nextDay = nextDate.format(formatter);
        int scopeStart = analyzer.findDateRowIndex(logList, today, 0);
        int scopeEnd = analyzer.findDateRowIndex(logList, nextDay, 0);
        int nonEmptyRow = analyzer.findLastNonEmptyRowIndex(logList, 5);
        boolean isNew = false;
        if (scopeStart == -1 && scopeEnd == -1) {
            System.out.println("There is no scopeStart and no scopeEnd");
            scopeStart = nonEmptyRow + 2;
            scopeEnd = scopeStart + rowsToAdd;
            isNew = true;
        } else if (scopeEnd == -1) {
            System.out.println("There is no scopeEnd");
            scopeEnd = nonEmptyRow + rowsToAdd;
        } else {
            System.out.println("There are two scopes");
        }
        return new SheetScope(scopeStart, scopeEnd, nonEmptyRow, isNew);
    }

    public boolean hasPostings(SheetScope scope, List<List<Object>> logList, String stringToCheck) {
        if (logList == null || scope == null || stringToCheck == null) {
            return false;
        }
        int endIndex = Math.min(scope.getEndIndex(), logList.size());
        for (int i = scope.getStartIndex(); i < endIndex; i++) {
            var row = logList.get(i);
            if (row.contains(stringToCheck)) {
                return true;
            }
        }
        return false;
    }
}
