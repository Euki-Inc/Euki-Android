package com.kollectivemobile.euki.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String mTitle;
    private List<Pair<String, List<Integer>>> mOptions;
    private Integer mAnswerIndex;

    public Question(String title) {
        this.mTitle = title;
        this.mOptions = new ArrayList<>();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Pair<String, List<Integer>>> getOptions() {
        return mOptions;
    }

    public void setOptions(List<Pair<String, List<Integer>>> options) {
        mOptions = options;
    }

    public Integer getAnswerIndex() {
        return mAnswerIndex;
    }

    public void setAnswerIndex(Integer answerIndex) {
        mAnswerIndex = answerIndex;
    }
}
