package com.kollectivemobile.euki.model.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(long dateLong) {
        return new Date(dateLong);
    }

    @TypeConverter
    public static long fromDate(Date date) {
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }
}
