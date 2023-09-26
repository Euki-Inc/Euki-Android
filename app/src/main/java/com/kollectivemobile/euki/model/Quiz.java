package com.kollectivemobile.euki.model;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String mInstructions;
    private List<Question> mQuestions;

    public Quiz(String instructions) {
        this.mInstructions = instructions;
        this.mQuestions = new ArrayList<>();
    }

    public String getInstructions() {
        return mInstructions;
    }

    public void setInstructions(String instructions) {
        mInstructions = instructions;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    public void setAnswer(int questionIndex, int answerIndex) {
        mQuestions.get(questionIndex).setAnswerIndex(answerIndex);
        EventBus.getDefault().post(this);
    }
}
