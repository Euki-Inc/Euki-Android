package com.kollectivemobile.euki.ui.calendar.appointments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.Utils;

import java.util.Date;

public class AppointmentsActivity extends BaseActivity {
    public static String DATE_PARAM_KEY = "DATE_PARAM_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        showEukiLogo();
        if (savedInstanceState == null) {
            Date date = new Date();
            String dateString = getIntent().getStringExtra(DATE_PARAM_KEY);
            if (dateString != null) {
                date = DateUtils.toDate(dateString, DateUtils.eeeMMMdyyyyhmma);
            }
            replaceFragment(AppointmentsFragment.newInstance(date), false);
        }
    }

    public static Intent makeIntent(Context context, Date date) {
        Intent intent = new Intent(context, AppointmentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DATE_PARAM_KEY, DateUtils.toString(date, DateUtils.eeeMMMdyyyyhmma));
        intent.putExtras(bundle);
        return intent;
    }
}
