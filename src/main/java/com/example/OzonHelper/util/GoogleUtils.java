package com.example.OzonHelper.util;

public class GoogleUtils {

    public static String buildRange(String title, String columnStart, String columnEnd, int scopeStartRow) {
        int tableScopeStartRow = scopeStartRow + 1;
        return title + "!" + columnStart + tableScopeStartRow + ":" + columnEnd + tableScopeStartRow;
    }
}
