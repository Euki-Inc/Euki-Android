package com.kollectivemobile.euki.ui.browser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;

public class BrowserActivity extends BaseActivity {
    final static String URL_KEY = "URL_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        if (savedInstanceState == null) {
            String url = getIntent().getStringExtra(URL_KEY);
            replaceFragment(BrowserFragment.newInstance(url), false);
        }
    }

    public static Intent makeIntent(Context context, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY, url);
        intent.putExtras(bundle);
        return intent;
    }
}
