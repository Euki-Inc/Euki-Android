package com.kollectivemobile.euki.manager;

import android.util.Pair;

import com.kollectivemobile.euki.model.Quiz;
import com.kollectivemobile.euki.model.QuizMethod;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.List;

public interface QuizManager {
    List<QuizMethod> getMethods();
    void getContraceptionQuiz(EukiCallback<Quiz> callback);
    Pair<String, List<Integer>> getresultContraception(Quiz quiz);
}
