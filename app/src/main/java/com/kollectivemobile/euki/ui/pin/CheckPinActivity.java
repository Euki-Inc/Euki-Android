package com.kollectivemobile.euki.ui.pin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;

public class CheckPinActivity extends BaseActivity {
    public static String SHOULD_SHOW_MAIN_KEY = "SHOULD_SHOW_MAIN_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_no_bar_main);
        if (savedInstanceState == null) {
            Boolean shouldShowMainViewController = getIntent().getBooleanExtra(SHOULD_SHOW_MAIN_KEY, true);
            replaceFragment(CheckPinFragment.newInstance(shouldShowMainViewController), false);
        }
    }

    public static Intent makeIntent(Context context, Boolean shouldShowMainViewController) {
        Intent intent = new Intent(context, CheckPinActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOULD_SHOW_MAIN_KEY, shouldShowMainViewController);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
