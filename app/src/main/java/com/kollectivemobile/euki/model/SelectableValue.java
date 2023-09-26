package com.kollectivemobile.euki.model;

public class SelectableValue {
    private String mIconName;
    private String mTitle;
    private Integer mCounter;

    public SelectableValue(String iconName, String title, Integer counter) {
        this.mIconName = iconName;
        this.mTitle = title;
        this.mCounter = counter;
    }

    public String getIconName() {
        return mIconName;
    }

    public void setIconName(String iconName) {
        mIconName = iconName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Integer getCounter() {
        return mCounter;
    }

    public void setCounter(Integer counter) {
        mCounter = counter;
    }
}
