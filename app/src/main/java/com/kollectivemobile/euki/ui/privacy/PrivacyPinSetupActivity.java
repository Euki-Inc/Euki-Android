package com.kollectivemobile.euki.ui.privacy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;

public class PrivacyPinSetupActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        if (savedInstanceState == null) {
            replaceFragment(PrivacyPinSetupFragment.newInstance(), false);
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PrivacyPinSetupActivity.class);
        return intent;
    }
}
