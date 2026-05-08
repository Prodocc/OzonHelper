package com.example.OzonHelper.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SheetScope {
    private int startIndex;
    private int endIndex;
    private int lastDataRowIndex;
    private boolean isNew;
}
