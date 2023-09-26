package com.kollectivemobile.euki.ui.cycle.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        replaceFragment(SettingsFragment.newInstance(), false);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }
}
