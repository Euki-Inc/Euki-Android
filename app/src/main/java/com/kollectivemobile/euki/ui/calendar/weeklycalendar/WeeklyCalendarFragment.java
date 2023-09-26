package com.kollectivemobile.euki.ui.calendar.weeklycalendar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.adapter.CalendarWeekAdapter;
import com.kollectivemobile.euki.ui.common.listeners.CalendarDayListener;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.SnapToBlock;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class WeeklyCalendarFragment extends BaseFragment implements CalendarDayListener {
    @Inject CalendarManager mCalendarManager;
    @Inject AppSettingsManager mAppSettingsManager;;

    @BindView(R.id.rv_main) RecyclerView rvMain;

    private WeeklyCalendarListener listener;
    private Map<String, CalendarItem> mCalendarItems;
    private List<DayItem> todayItems;
    private DayItem todayItem;
    private Date startDate;
    private DayItem startItem;
    private Date selectedDate;

    private CalendarWeekAdapter mAdapter;

    public static WeeklyCalendarFragment newInstance(Date startDate, Date selectedDate, WeeklyCalendarListener listener) {
        WeeklyCalendarFragment fragment = new WeeklyCalendarFragment();
        fragment.startDate = startDate;
        fragment.selectedDate = selectedDate;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
        showDateItem();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_calendar, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCalendarItems();
    }

    private void setUIElements() {
        mCalendarItems = new HashMap<>();

        Pair<List<DayItem>, Pair<DayItem, DayItem>> pair = DayItem.getItems(startDate);
        todayItems = pair.first;
        todayItem = pair.second.first;
        startItem = pair.second.second;

        mAdapter = new CalendarWeekAdapter(getActivity(), selectedDate, this);
        rvMain.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMain.setLayoutManager(layoutManager);

        SnapToBlock snapToBlock = new SnapToBlock(7);
        snapToBlock.attachToRecyclerView(rvMain);
    }

    private void showDateItem() {
        DayItem item = null;

        if (startItem != null) {
            item = startItem;
        } else if (todayItem != null) {
            item = todayItem;
        }

        if (item != null) {
            Integer weekDay = DateUtils.getDayOfWeek(item.getDate());
            rvMain.getLayoutManager().scrollToPosition(item.getIndex() - (weekDay - 1));
            daySelected(item.getDate());
        }
    }

    private void requestCalendarItems() {
        mCalendarManager.getCalendarItems(new EukiCallback<Map<String, CalendarItem>>() {
            @Override
            public void onSuccess(Map<String, CalendarItem> stringCalendarItemMap) {
                mCalendarItems.clear();
                mCalendarItems.putAll(stringCalendarItemMap);
                updateUIElements();
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }

    private void updateUIElements() {
        CalendarFilter filter = new CalendarFilter();

        for (FilterItem item : mAppSettingsManager.filterItems()) {
            if (item.getTitle().equals("bleeding")) {
                filter.setBleedingOn(item.getOn());
            } if (item.getTitle().equals("emotions")) {
                filter.setEmotionsOn(item.getOn());
            } if (item.getTitle().equals("body")) {
                filter.setBodyOn(item.getOn());
            } if (item.getTitle().equals("sexual_activity")) {
                filter.setSexualActivityOn(item.getOn());
            } if (item.getTitle().equals("contraception")) {
                filter.setContraceptionOn(item.getOn());
            } if (item.getTitle().equals("test")) {
                filter.setTestOn(item.getOn());
            } if (item.getTitle().equals("appointment")) {
                filter.setAppointmentOn(item.getOn());
            } if (item.getTitle().equals("note")) {
                filter.setNoteOn(item.getOn());
            }
        }

        mAdapter.updateObjects(todayItems, mCalendarItems, filter);
    }

    public void reloadData() {
        requestCalendarItems();
    }

    @Override
    public void daySelected(Date date) {
        mCalendarManager.getCalendarItem(date, new EukiCallback<CalendarItem>() {
            @Override
            public void onSuccess(CalendarItem calendarItem) {
                listener.selectedDate(calendarItem);
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }
}

