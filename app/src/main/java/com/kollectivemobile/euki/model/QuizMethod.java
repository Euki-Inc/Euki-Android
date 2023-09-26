package com.kollectivemobile.euki.model;

public class QuizMethod {
    private String mTitle;
    private String mImageName;
    private Boolean isSelected;

    public QuizMethod(String title, String imageName) {
        mTitle = title;
        mImageName = imageName;
        isSelected = false;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
