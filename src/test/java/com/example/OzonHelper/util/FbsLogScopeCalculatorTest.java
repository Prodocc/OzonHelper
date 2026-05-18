package com.example.OzonHelper.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.OzonHelper.util.SheetTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

class FbsLogScopeCalculatorTest {
    private FbsLogScopeCalculator calculator;
    private SheetAnalyzer analyzer;

    @BeforeEach
    public void init() {
        this.analyzer = new SheetAnalyzer();
        this.calculator = new FbsLogScopeCalculator(analyzer);
    }

    @Test
    public void hasPosting_WhenHasPostingAtStart_ShouldReturnTrue() {
        List<List<Object>> table = createTable(3, 4);
        SheetScope scope = createDefaultScope();
        String stringToCheck = "targetString";

        table.get(0).set(0, stringToCheck);

        assertThat(calculator.hasPostings(scope, table, stringToCheck)).isTrue();
    }

    @Test
    public void hasPosting_WhenHasPostingAtEnd_ShouldReturnTrue() {
        int row = 3;
        int col = 4;
        List<List<Object>> table = createTable(row, col);
        SheetScope scope = createDefaultScope();
        String stringToCheck = "targetString";

        table.get(row - 1).set(col - 1, stringToCheck);

        assertThat(calculator.hasPostings(scope, table, stringToCheck)).isTrue();
    }

    @Test
    public void hasPosting_WhenNoPosting_ShouldReturnFalse() {
        List<List<Object>> table = createTable(3, 4);
        SheetScope scope = createDefaultScope();
        String stringToCheck = "targetString";

        assertThat(calculator.hasPostings(scope, table, stringToCheck)).isFalse();
    }

    @Test
    public void hasPosting_WhenHasNoTargetPostings_ShouldReturnFalse() {
        List<List<Object>> table = createTable(3, 4);
        SheetScope scope = createDefaultScope();
        String stringToCheck = "targetString";

        table.get(0).set(0, "nonTargetPostings");

        assertThat(calculator.hasPostings(scope, table, stringToCheck)).isFalse();
    }

    @Test
    public void hasPosting_WhenScopeIsLargerThenTable_ShouldNotThrowException() {
        List<List<Object>> table = createTable(3, 4);
        SheetScope scope = createScopeWithBounds(0, 10, 4);
        String stringToCheck = "targetString";

        table.get(0).set(0, stringToCheck);

        assertThat(calculator.hasPostings(scope, table, stringToCheck)).isTrue();
    }

    @Test
    public void hasPosting_WhenTargetIsOutsideScope_ShouldReturnFalse() {
        List<List<Object>> table = createTable(10, 4);
        SheetScope scope = createScopeWithBounds(0, 5, 4);
        String stringToCheck = "targetString";

        table.get(7).set(0, stringToCheck);

        assertThat(calculator.hasPostings(scope, table, stringToCheck)).isFalse();
    }

    @Test
    public void hasPosting_WhenTableIsNull_ShouldReturnFalse() {
        SheetScope scope = createScopeWithBounds(0, 5, 4);
        String stringToCheck = "targetString";

        assertThat(calculator.hasPostings(scope, null, stringToCheck)).isFalse();
    }

    @Test
    public void hasPosting_WhenTargetIsNull_ShouldReturnFalse() {
        List<List<Object>> table = createTable(10, 4);
        SheetScope scope = createScopeWithBounds(0, 5, 4);

        assertThat(calculator.hasPostings(scope, table, null)).isFalse();
    }

    @Test
    public void hasPosting_WhenScopeIsNull_ShouldReturnFalse() {
        List<List<Object>> table = createTable(10, 4);
        String stringToCheck = "targetString";

        assertThat(calculator.hasPostings(null, table, stringToCheck)).isFalse();
    }

    @Test
    public void calculateScope_WhenThereAreNoScopes_ShouldReturnCorrectScope() {
        List<List<Object>> table = createTable(10, 4);
        int rowsToAdd = 6;
        SheetScope sheetScope = calculator.calculateScope(table, rowsToAdd);

        assertThat(sheetScope.getStartIndex()).isEqualTo(1);
        assertThat(sheetScope.getEndIndex()).isEqualTo(1 + rowsToAdd);
        assertThat(sheetScope.getLastDataRowIndex()).isEqualTo(-1);

    }

    @Test
    public void calculateScope_WhenThereAreOnlyStartScopeAtStart_ShouldReturnCorrectScope() {
        List<List<Object>> table = createTable(10, 4);
        int rowsToAdd = 6;
        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(0).set(0, startAndEndDateForTest[0]);

        SheetScope sheetScope = calculator.calculateScope(table, rowsToAdd);

        assertThat(sheetScope.getStartIndex()).isEqualTo(0);
        assertThat(sheetScope.getEndIndex()).isEqualTo(rowsToAdd);
        assertThat(sheetScope.getLastDataRowIndex()).isEqualTo(0);
    }

    @Test
    public void calculateScope_WhenThereAreOnlyStartScopeAtEnd_ShouldReturnCorrectScope() {
        List<List<Object>> table = createTable(10, 4);
        int rowsToAdd = 6;
        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(table.size() - 1).set(0, startAndEndDateForTest[0]);

        SheetScope sheetScope = calculator.calculateScope(table, rowsToAdd);

        assertThat(sheetScope.getStartIndex()).isEqualTo(table.size() - 1);
        assertThat(sheetScope.getEndIndex()).isEqualTo(table.size() - 1 + rowsToAdd);
        assertThat(sheetScope.getLastDataRowIndex()).isEqualTo(table.size() - 1);
    }

    @Test
    public void calculateScope_WhenThereAreTwoScopes_ShouldReturnCorrectScope() {
        List<List<Object>> table = createTable(10, 4);
        int rowToAdd = 6;
        String[] startAndEndDateForTest = SheetTestDataFactory.getStartAndEndDateForTest();

        table.get(0).set(0, startAndEndDateForTest[0]); // set startScope
        table.get(table.size() / 2).set(0, startAndEndDateForTest[1]); // set endScope

        SheetScope sheetScope = calculator.calculateScope(table, rowToAdd);

        assertThat(sheetScope.getStartIndex()).isEqualTo(0);
        assertThat(sheetScope.getEndIndex()).isEqualTo(table.size() / 2);
        assertThat(sheetScope.getLastDataRowIndex()).isEqualTo(5);
    }

}