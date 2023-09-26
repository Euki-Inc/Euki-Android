package com.kollectivemobile.euki.ui.calendar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.InsetDecoration;
import com.kollectivemobile.euki.ui.common.adapter.CalendarListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class CalendarListFragment extends BaseFragment implements CalendarListAdapter.CalendarListListener {
    @Inject CalendarManager mCalendarManager;

    @BindView(R.id.rv_main) RecyclerView rvMain;

    private CalendarFilter mCalendarFilter;
    private CalendarListAdapter mAdapter;
    private List<CalendarItem> mCalendarItems;
    private List<CalendarItem> mFilteredItems;

    public static CalendarListFragment newInstance(CalendarFilter calendarFilter) {
        Bundle args = new Bundle();
        CalendarListFragment fragment = new CalendarListFragment();
        fragment.mCalendarFilter = calendarFilter;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCalendarItems();
    }

    @Override
    public String getTitle() {
        return getString(R.string.calendar);
    }

    private void setUIElements() {
        mCalendarItems = new ArrayList<>();
        mFilteredItems = new ArrayList<>();
        mAdapter = new CalendarListAdapter(getActivity(), this);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMain.addItemDecoration(new InsetDecoration(getContext(), InsetDecoration.VERTICAL_LIST));
    }

    private void requestCalendarItems() {
        mCalendarManager.getDayscalendarItems(new EukiCallback<List<CalendarItem>>() {
            @Override
            public void onSuccess(List<CalendarItem> calendarItems) {
                mCalendarItems.clear();
                mCalendarItems.addAll(calendarItems);
                filterItems();
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }

    private void filterItems() {
        mFilteredItems.clear();

        for (CalendarItem calendarItem : mCalendarItems) {
            if (mCalendarFilter.showAll()) {
                mFilteredItems.add(calendarItem);
            } else if ((mCalendarFilter.getBleedingOn() && calendarItem.hasBleeding()) ||
                       (mCalendarFilter.getEmotionsOn() && calendarItem.hasEmotions()) ||
                       (mCalendarFilter.getBodyOn() && calendarItem.hasBody()) ||
                       (mCalendarFilter.getSexualActivityOn() && calendarItem.hasSexualActivity()) ||
                       (mCalendarFilter.getContraceptionOn() && calendarItem.hasContraception()) ||
                       (mCalendarFilter.getTestOn() && calendarItem.hasTest()) ||
                       (mCalendarFilter.getAppointmentOn() && calendarItem.hasAppointment()) ||
                       (mCalendarFilter.getNoteOn() && calendarItem.hasNote())) {
                mFilteredItems.add(calendarItem);
            }
        }

        mAdapter.update(mFilteredItems, mCalendarFilter);
    }

    @Override
    public void daySelected(int position) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(CalendarFilter calendarFilter) {
        mCalendarFilter = calendarFilter;
        mAdapter.update(mFilteredItems, mCalendarFilter);
    }
}
