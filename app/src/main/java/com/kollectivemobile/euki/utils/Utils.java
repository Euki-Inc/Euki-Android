package com.kollectivemobile.euki.utils;

import android.util.TypedValue;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static JSONObject getJsonFromAssets(String filename) {
        try {
            String jsonFilename = "json/" + filename + ".json";
            InputStream is = App.getContext().getAssets().open(jsonFilename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = reader.readLine();
            StringBuilder stringBuilder = new StringBuilder();

            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getLocalized(String key) {
        int resId = getResourceId(key, "string");
        if (resId == -1) {
            return key;
        }
        try {
            return App.getContext().getString(resId);
        } catch (Exception e) {
            return key;
        }
    }

    public static int getImageId(String key) {
        int resId = getResourceId(key, "drawable");
        if (resId == -1) {
            return R.drawable.ic_nav_logo;
        }
        return resId;
    }

    public static int getResourceId(String key, String resourceType) {
        try {
            int resourceId = App.getContext().getResources().getIdentifier(key, resourceType, App.getContext().getPackageName());
            return resourceId;
        } catch (Exception e) {
            return -1;
        }
    }

    public static Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static int dpFromInt(int pixels) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, App.getContext().getResources().getDisplayMetrics()));
    }

    public static String decamelcase(String string) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return string.replaceAll(regex, replacement).toLowerCase();
    }

    public static String suffix(String value) {
        try {
            double intValue = Double.parseDouble(value);
            return intValue > 1 ? "s" : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
