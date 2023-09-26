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
import com.kollectivemobile.euki.ui.cycle.CycleSummaryFragment;
import com.kollectivemobile.euki.ui.cycle.DaySummaryFragment;

public class CycleFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public CycleFragmentAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return DaySummaryFragment.newInstance();
        }
        return CycleSummaryFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.day_summary);
        }
        return mContext.getString(R.string.cycle_summary);
    }
}
