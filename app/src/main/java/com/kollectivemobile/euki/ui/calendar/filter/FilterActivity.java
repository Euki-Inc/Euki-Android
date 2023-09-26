package com.kollectivemobile.euki.ui.calendar.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.utils.Utils;

public class FilterActivity extends BaseActivity {
    final static String CALENDAR_FILTER_KEY = "CALENDAR_FILTER_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        if (savedInstanceState == null) {
            CalendarFilter calendarFilter = getIntent().getParcelableExtra(CALENDAR_FILTER_KEY);
            replaceFragment(FilterFragment.newInstance(calendarFilter), false);
        }
    }

    public static Intent makeIntent(Context context, CalendarFilter calendarFilter) {
        Intent intent = new Intent(context, FilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CALENDAR_FILTER_KEY, calendarFilter);
        intent.putExtras(bundle);
        return intent;
    }
}
