package com.kollectivemobile.euki.ui.calendar.filter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.adapter.CalendarFilterAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FilterFragment extends BaseFragment implements CalendarFilterAdapter.CalendarFilterListener {
    @BindView(R.id.rv_main) RecyclerView rvMain;

    private CalendarFilter mCalendarFilter;
    private CalendarFilterAdapter mAdapter;
    private List<FilterItem> mFilterItems;

    public static FilterFragment newInstance(CalendarFilter calendarFilter) {
        Bundle args = new Bundle();
        FilterFragment fragment = new FilterFragment();
        fragment.mCalendarFilter = calendarFilter;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        createFilterItems();
        setUIElements();
        setHasOptionsMenu(true);
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_filter, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_cancel:
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getTitle() {
        return getString(R.string.filter);
    }

    private void createFilterItems() {
        List<FilterItem> filterItems = new ArrayList<>();
        filterItems.add(new FilterItem(R.color.bleeding, "bleeding", mCalendarFilter.getBleedingOn()));
        filterItems.add(new FilterItem(R.color.emotions, "emotions", mCalendarFilter.getEmotionsOn()));
        filterItems.add(new FilterItem(R.color.body, "body", mCalendarFilter.getBodyOn()));
        filterItems.add(new FilterItem(R.color.sexual_activity, "sexual_activity", mCalendarFilter.getSexualActivityOn()));
        filterItems.add(new FilterItem(R.color.contraception, "contraception", mCalendarFilter.getContraceptionOn()));
        filterItems.add(new FilterItem(R.color.test, "test", mCalendarFilter.getTestOn()));
        filterItems.add(new FilterItem(R.color.appointment, "appointment", mCalendarFilter.getAppointmentOn()));
        filterItems.add(new FilterItem(R.color.note, "note", mCalendarFilter.getNoteOn()));
        mFilterItems = filterItems;
    }

    private void setUIElements() {
        mAdapter = new CalendarFilterAdapter(getActivity(), mFilterItems, this);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void filterSelected(int position) {
        FilterItem filterItem = mFilterItems.get(position);
        filterItem.setOn(!filterItem.getOn());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clear() {
        for (FilterItem filterItem : mFilterItems) {
            filterItem.setOn(false);
        }
        showResults();
    }

    @Override
    public void showResults() {
        for (FilterItem filterItem : mFilterItems) {
            switch (filterItem.getTitle()) {
                case "bleeding":
                    mCalendarFilter.setBleedingOn(filterItem.getOn());
                    break;
                case "emotions":
                    mCalendarFilter.setEmotionsOn(filterItem.getOn());
                    break;
                case "body":
                    mCalendarFilter.setBodyOn(filterItem.getOn());
                    break;
                case "sexual_activity":
                    mCalendarFilter.setSexualActivityOn(filterItem.getOn());
                    break;
                case "contraception":
                    mCalendarFilter.setContraceptionOn(filterItem.getOn());
                    break;
                case "test":
                    mCalendarFilter.setTestOn(filterItem.getOn());
                    break;
                case "appointment":
                    mCalendarFilter.setAppointmentOn(filterItem.getOn());
                    break;
                case "note":
                    mCalendarFilter.setNoteOn(filterItem.getOn());
                    break;
            }
        }
        EventBus.getDefault().post(mCalendarFilter);
        getActivity().finish();
    }
}
