package com.kollectivemobile.euki.model.converters;

import androidx.room.TypeConverter;
import android.graphics.Color;

public class ColorConverter {
    @TypeConverter
    public static Integer toColor(String colorString) {
        return Color.parseColor(colorString);
    }

    @TypeConverter
    public static String fromColor(Integer color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
