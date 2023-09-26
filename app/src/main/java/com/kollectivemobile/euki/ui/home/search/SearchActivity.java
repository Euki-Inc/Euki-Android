package com.kollectivemobile.euki.ui.home.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.utils.Utils;

public class SearchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        showEukiLogo();
        if (savedInstanceState == null) {
            replaceFragment(SearchFragment.newInstance(), false);
        }
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }
}
