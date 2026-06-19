package com.example.OzonHelper.enums;

import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.Sheet;

public enum SheetColors {
    FBS_LIST_DATE_COLOR(255, 217, 102);

    private final float red;
    private final float green;
    private final float blue;

    SheetColors(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color toGoogleColor() {
        Color color = new Color();
        color.setRed(red / 255.0f);
        color.setGreen(green / 255.0f);
        color.setBlue(blue / 255.0f);

        return color;
    }
}
