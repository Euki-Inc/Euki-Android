package com.kollectivemobile.euki.ui.calendar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.manager.ReminderManager;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.calendar.filter.FilterActivity;
import com.kollectivemobile.euki.ui.calendar.reminders.RemindersActivity;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.adapter.CalendarFragmentAdapter;
import com.kollectivemobile.euki.ui.common.views.NoSwipeViewPager;
import com.kollectivemobile.euki.ui.common.views.SegmentedButton;
import com.kollectivemobile.euki.ui.common.views.SegmentedButtonListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class CalendarFragment extends BaseFragment {
    @BindView(R.id.vp_main) NoSwipeViewPager vpMain;
    @BindView(R.id.sb_sections) SegmentedButton sbSections;
    @Inject CalendarManager mCalendarManager;
    @Inject ReminderManager mReminderManager;

    public static CalendarFragment newInstance() {
        Bundle args = new Bundle();
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);

        final com.kollectivemobile.euki.ui.common.views.MenuItem filterItem =
                (com.kollectivemobile.euki.ui.common.views.MenuItem)menu.getItem(1).getActionView();
        filterItem.setTitle(getString(R.string.filter));
        filterItem.updateCount(mCalendarManager.getCalendarFilter().getFiltersCount());
        filterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FilterActivity.makeIntent(getActivity(), mCalendarManager.getCalendarFilter()));
            }
        });

        final com.kollectivemobile.euki.ui.common.views.MenuItem reminderItem =
                (com.kollectivemobile.euki.ui.common.views.MenuItem)menu.getItem(0).getActionView();
        reminderItem.setTitle(getString(R.string.reminders));
        reminderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RemindersActivity.makeIntent(getActivity()));
            }
        });
        mReminderManager.getReminders(new EukiCallback<List<ReminderItem>>() {
            @Override
            public void onSuccess(List<ReminderItem> reminderItems) {
                reminderItem.updateCount(reminderItems.size());
            }

            @Override
            public void onError(ServerError serverError) {
                reminderItem.updateCount(0);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_reminders:
                startActivity(RemindersActivity.makeIntent(getActivity()));
                break;
            case R.id.item_filter:
                startActivity(FilterActivity.makeIntent(getActivity(), mCalendarManager.getCalendarFilter()));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getTitle() {
        return getString(R.string.calendar);
    }

    private void setUIElements() {
        CalendarFragmentAdapter adapter = new CalendarFragmentAdapter(getActivity(), getChildFragmentManager(), mCalendarManager.getCalendarFilter());
        vpMain.setAdapter(adapter);
        vpMain.setPagingEnabled(false);

        sbSections.setListener(new SegmentedButtonListener() {
            @Override
            public void onSegmentedChanged(Integer index) {
                vpMain.setCurrentItem(index);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(CalendarFilter calendarFilter) {
        mCalendarManager.updateCalendarFilter(calendarFilter);
    }
}
