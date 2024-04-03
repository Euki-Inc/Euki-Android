package com.kollectivemobile.euki.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.QuizType;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.home.search.SearchFragment;
import com.kollectivemobile.euki.utils.Utils;

public class QuizActivity extends BaseActivity {

    public final static String QUIZ_TYPE = "QUIZ_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        if (savedInstanceState == null) {
            QuizType quizType = (QuizType) getIntent().getSerializableExtra(QUIZ_TYPE);

            if (quizType != null) {
                replaceFragment(QuizFragment.newInstance(quizType), false);
            }
        }
    }

    public static Intent makeIntent(Context context, QuizType quizType) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(QUIZ_TYPE, quizType);
        return intent;
    }
}
