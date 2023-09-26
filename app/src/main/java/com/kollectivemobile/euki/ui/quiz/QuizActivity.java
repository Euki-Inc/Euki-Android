package com.kollectivemobile.euki.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.home.search.SearchFragment;
import com.kollectivemobile.euki.utils.Utils;

public class QuizActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        if (savedInstanceState == null) {
            replaceFragment(QuizFragment.newInstance(), false);
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, QuizActivity.class);
        return intent;
    }
}
