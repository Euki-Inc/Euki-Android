package com.kollectivemobile.euki.ui.track;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.BaseActivity;

import java.util.Date;

public class TrackActivity extends BaseActivity {
    final static String DATE_KEY = "DATE_KEY";
    final static String CALENDAR_ITEM_KEY = "CALENDAR_ITEM_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        if (savedInstanceState == null) {
            Date date = null;
            String dateValue = getIntent().getStringExtra(DATE_KEY);
            if (dateValue != null) {
                date = new Gson().fromJson(dateValue, new TypeToken<Date>(){}.getType());
            }

            CalendarItem calendarItem = null;
            String itemValue = getIntent().getStringExtra(CALENDAR_ITEM_KEY);
            if (itemValue != null) {
                calendarItem = new Gson().fromJson(itemValue, new TypeToken<CalendarItem>(){}.getType());
            }
            replaceFragment(TrackFragment.newInstance(date, calendarItem), false);
        }
    }

    public static Intent makeIntent(Context context, Date date, CalendarItem calendarItem) {
        Intent intent = new Intent(context, TrackActivity.class);
        Bundle bundle = new Bundle();
        if (calendarItem != null) {
            bundle.putString(DATE_KEY, new Gson().toJson(date));
            bundle.putString(CALENDAR_ITEM_KEY, new Gson().toJson(calendarItem));
        }
        intent.putExtras(bundle);
        return intent;
    }
}
