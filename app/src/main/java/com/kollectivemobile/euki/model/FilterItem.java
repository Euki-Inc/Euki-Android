package com.kollectivemobile.euki.model;

import com.kollectivemobile.euki.R;

import java.util.ArrayList;
import java.util.List;

public class FilterItem {
    private Integer mColor;
    private String mTitle;
    private Boolean mIsOn;

    public FilterItem(Integer color, String title, Boolean isOn) {
        this.mColor = color;
        this.mTitle = title;
        this.mIsOn = isOn;
    }

    public Integer getColor() {
        return mColor;
    }

    public void setColor(Integer color) {
        mColor = color;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Boolean getOn() {
        return mIsOn;
    }

    public void setOn(Boolean on) {
        mIsOn = on;
    }

    public static List<FilterItem> getAllCategories() {
        List<FilterItem> allCategories = new ArrayList<>();
        allCategories.add(new FilterItem(R.color.bleeding, "bleeding", true));
        allCategories.add(new FilterItem(R.color.emotions, "emotions", true));
        allCategories.add(new FilterItem(R.color.body, "body", true));
        allCategories.add(new FilterItem(R.color.sexual_activity, "sexual_activity", true));
        allCategories.add(new FilterItem(R.color.contraception, "contraception", true));
        allCategories.add(new FilterItem(R.color.test, "test", true));
        allCategories.add(new FilterItem(R.color.appointment, "appointment", true));
        allCategories.add(new FilterItem(R.color.note, "note", true));
        return allCategories;
    }
}
