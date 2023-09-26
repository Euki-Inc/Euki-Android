package com.kollectivemobile.euki.utils.strings;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static Spannable processString(String input){
        String[] lines = input.split(System.getProperty("line.separator"));

        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < lines.length; i++) {
            CharSequence line = lines[i] + (i < lines.length-1 ? "\n" : "");
            Spannable spannable = new SpannableString(line);

            String lineString = line.toString();
            int index = lineString.indexOf("- ");
            if (index != -1){
                String substring = lineString.substring(0, 2);
                lineString = lineString.replace(substring, "");
                spannable = new SpannableString(lineString);
                spannable.setSpan(new BulletIndentSpan(20, 30), 0, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            sb.append(spannable);
        }
        return sb;
    }

    public static List<String> phoneNumbers(String input) {
        List<String> results = new ArrayList<>();

        String patternString = "(?:(?:\\+?([1-9]|[0-9][0-9]|[0-9][0-9][0-9])\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([0-9][1-9]|[0-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9A-Z]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            results.add(matcher.group());
        }

        return results;
    }

    public static String capitalize(String input) {
        if (input == null) {
            return null;
        }

        if (input.length() < 2) {
            return input.toUpperCase();
        }

        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String capitalizeAll(String input) {
        if (input == null) {
            return null;
        }

        String[] words = input.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String capitalized = capitalize(word);
            input = input.replaceAll(word, capitalized);
        }
        return input;
    }

}
