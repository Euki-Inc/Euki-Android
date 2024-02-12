package com.kollectivemobile.euki.utils;

import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.listeners.LinkListener;
import com.kollectivemobile.euki.utils.strings.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class TextUtils {
    static public Spannable getSpannable(String text, Map<String, String> links, final LinkListener linkListener, List<String> boldStrings) {
        Spannable spannable = StringUtils.processString(text);
        String newText = spannable.toString();

        try {
        if (links != null) {
            for (String link : links.keySet()) {
                final String url = links.get(link);
                for (Integer index : indexes(newText, link)) {
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            linkListener.linkClicked(url);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(ContextCompat.getColor(App.getContext(), R.color.link));
                        }
                    };
                    spannable.setSpan(clickableSpan, index, index + link.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        for (final String phoneNumber : StringUtils.phoneNumbers(newText)) {
            for (Integer index : indexes(newText, phoneNumber)) {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        linkListener.phoneClicked(phoneNumber);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(ContextCompat.getColor(App.getContext(), R.color.link));
                    }
                };
                spannable.setSpan(clickableSpan, index, index + phoneNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (boldStrings != null) {
            for (String string : boldStrings) {
                String boldString = Utils.getLocalized(string);
                Integer index = newText.indexOf(boldString);
                if (index > -1) {
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    spannable.setSpan(boldSpan, index, index + boldString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spannable;
        } catch (Exception e){
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            Timber.d("**** %s", sw.getBuffer().toString());
        } finally {
            return spannable;
        }
    }

    static public List<Integer> indexes(String text, String searchedString) {
        List<Integer> indexes = new ArrayList<>();
        String lowerCaseTextString = text.toLowerCase();
        String lowerCaseWord = searchedString.toLowerCase();

        int index = 0;
        while(index != -1){
            index = lowerCaseTextString.indexOf(lowerCaseWord, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }
}
