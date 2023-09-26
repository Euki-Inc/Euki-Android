package com.kollectivemobile.euki.ui.calendar.reminders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.utils.Utils;

public class RemindersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        showEukiLogo();
        if (savedInstanceState == null) {
            replaceFragment(RemindersFragment.newInstance(), false);
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, RemindersActivity.class);
        return intent;
    }
}
