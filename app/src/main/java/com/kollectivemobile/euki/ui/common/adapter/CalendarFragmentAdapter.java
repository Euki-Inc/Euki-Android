package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.ui.calendar.CalendarListFragment;
import com.kollectivemobile.euki.ui.calendar.CalendarMonthFragment;

public class CalendarFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private CalendarFilter mCalendarFilter;

    public CalendarFragmentAdapter(Context context, FragmentManager fragmentManager, CalendarFilter calendarFilter) {
        super(fragmentManager);
        mContext = context;
        mCalendarFilter = calendarFilter;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return CalendarMonthFragment.newInstance(mCalendarFilter);
        }
        return CalendarListFragment.newInstance(mCalendarFilter);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.calendar);
        }
        return mContext.getString(R.string.list);
    }
}
