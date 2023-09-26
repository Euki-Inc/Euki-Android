package com.kollectivemobile.euki.ui.onboarding;

import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;

public class OnboardingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_no_bar_main);
        replaceFragment(Onboarding1Fragment.newInstance(), false);
    }
}
