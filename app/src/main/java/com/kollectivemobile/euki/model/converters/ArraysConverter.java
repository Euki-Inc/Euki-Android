package com.kollectivemobile.euki.model.converters;

import androidx.room.TypeConverter;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kollectivemobile.euki.model.FilterItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Range;

public class ArraysConverter {
    @TypeConverter
    public static List<Integer> toList(String listString) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(listString, listType);
    }

    @TypeConverter
    public static String fromList(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<String> toListString(String listString) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(listString, listType);
    }

    @TypeConverter
    public static String fromListString(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static Map<String, String> toListPair(String mapString) {
        Type listType = new TypeToken<Map<String, String>>() {}.getType();
        return new Gson().fromJson(mapString, listType);
    }

    @TypeConverter
    public static String fromListPair(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @TypeConverter
    public static List<FilterItem> toListFilterItems(String listString) {
        if (listString == null) {
            return null;
        }
        Type listType = new TypeToken<List<FilterItem>>() {}.getType();
        return new Gson().fromJson(listString, listType);
    }

    @TypeConverter
    public static String fromListFilterItems(List<FilterItem> list) {
        if (list == null) {
            return null;
        }

        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Range<Date>> toListRangeDate(String listString) {
        if (listString == null) {
            return null;
        }

        List<Range<Date>> list = new ArrayList<>();

        try {
            Type listType = new TypeToken<List<Pair<Date, Date>>>() {}.getType();
            List<Pair<Date, Date>> listPair = new Gson().fromJson(listString, listType);

            for (Pair<Date, Date> pair : listPair) {
                list.add(Range.between(pair.first, pair.second));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @TypeConverter
    public static String fromListRangeDate(List<Range<Date>> list) {
        if (list == null) {
            return null;
        }

        List<Pair<Date, Date>> listPairs = new ArrayList<>();
        for (Range<Date> range : list) {
            listPairs.add(new Pair<>(range.getMinimum(), range.getMaximum()));
        }

        Gson gson = new Gson();
        return gson.toJson(listPairs);
    }
}
