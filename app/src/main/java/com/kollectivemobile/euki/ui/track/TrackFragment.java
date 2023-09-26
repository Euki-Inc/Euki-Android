package com.kollectivemobile.euki.ui.track;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.calendar.weeklycalendar.WeeklyCalendarFragment;
import com.kollectivemobile.euki.ui.calendar.weeklycalendar.WeeklyCalendarListener;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.dailylog.DailyLogFragment;
import com.kollectivemobile.euki.ui.dailylog.DailyLogListener;

import java.util.Date;

import butterknife.BindView;

public class TrackFragment extends BaseFragment implements WeeklyCalendarListener, DailyLogListener {
    @BindView(R.id.fl_header) FrameLayout flHeader;
    @BindView(R.id.fl_content) FrameLayout flContent;
    private Date mDate;
    private CalendarItem mCalendarItem;

    private WeeklyCalendarFragment weeklyCalendarFragment;
    private DailyLogFragment dailyLogFragment;

    public static TrackFragment newInstance(Date date, CalendarItem calendarItem) {
        Bundle args = new Bundle();
        TrackFragment fragment = new TrackFragment();
        fragment.mDate = date;
        fragment.mCalendarItem = calendarItem;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track, container, false);
    }

    private void setUIElements() {
        Date selectedDate;
        if (mCalendarItem != null && mCalendarItem.getDate()!= null) {
            selectedDate = mCalendarItem.getDate();
        } else if (mDate != null) {
            selectedDate = mDate;
        } else {
            selectedDate = new Date();
        }

        WeeklyCalendarFragment weeklyFragment = WeeklyCalendarFragment.newInstance(mDate, selectedDate, this);
        replace(R.id.fl_header, weeklyFragment, false);
        weeklyCalendarFragment = weeklyFragment;
    }

    private void replace(@IdRes int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction replaceTransaction = getFragmentManager().beginTransaction();
        replaceTransaction.replace(containerId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            replaceTransaction.addToBackStack(fragment.getClass().getName());
        }
        replaceTransaction.commit();
    }

    @Override
    public void selectedDate(final CalendarItem calendarItem) {
        if (dailyLogFragment != null) {
            dailyLogFragment.save(new EukiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    show(calendarItem);
                    reloadWeekCalendar();
                }

                @Override
                public void onError(ServerError serverError) {
                    show(calendarItem);
                    reloadWeekCalendar();
                }
            });
        } else {
            show(calendarItem);
        }
    }

    private void reloadWeekCalendar() {
        weeklyCalendarFragment.reloadData();
    }

    private void show(CalendarItem calendarItem) {
        DailyLogFragment fragment = DailyLogFragment.newInstance(calendarItem, this);
        replace(R.id.fl_content, fragment, false);
        dailyLogFragment = fragment;
    }

    @Override
    public void refreshFilterItems() {
        weeklyCalendarFragment.reloadData();
    }
}
