package com.kollectivemobile.euki.ui.calendar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.calendar.appointments.AppointmentsActivity;
import com.kollectivemobile.euki.ui.calendar.appointments.FutureAppointmentActivity;
import com.kollectivemobile.euki.ui.calendar.reminders.RemindersActivity;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.adapter.CalendarDayAdapter;
import com.kollectivemobile.euki.ui.track.TrackActivity;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.strings.StringUtils;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class CalendarDayFragment extends BaseFragment {
    @Inject CalendarManager mCalendarManager;

    @BindView(R.id.tv_day) TextView tvDay;
    @BindView(R.id.rl_logged_data) RelativeLayout rlLoggedData;
    @BindView(R.id.rv_main) RecyclerView rvMain;
    @BindView(R.id.ll_main_bottom) LinearLayout llMainBottom;
    @BindView(R.id.ll_past) LinearLayout llPast;
    @BindView(R.id.rl_future) RelativeLayout llFuture;

    private CalendarItem mCalendarItem;
    private CalendarDayAdapter mCalendarDayAdapter;

    public static CalendarDayFragment newInstance(CalendarItem calendarItem) {
        Bundle args = new Bundle();
        CalendarDayFragment fragment = new CalendarDayFragment();
        fragment.mCalendarItem = calendarItem;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
        updateUIElements();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCalendarItem();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_day, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Date date = mCalendarItem.getDate();
        Date nowDate = new Date();
        Boolean isFutureDate = true;
        if (DateUtils.isSameDate(date, nowDate) || nowDate.after(date)) {
            isFutureDate = false;
        }

        if (!isFutureDate && mCalendarItem.hasData()) {
            inflater.inflate(R.menu.menu_edit, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                startActivity(TrackActivity.makeIntent(getContext(), mCalendarItem.getDate(), mCalendarItem));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean showBack() {
        return true;
    }

    @Override
    public String getTitle() {
        return " ";
    }


    private void setUIElements() {
        mCalendarDayAdapter = new CalendarDayAdapter(getContext());
        rvMain.setAdapter(mCalendarDayAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false);
        rvMain.setLayoutManager(layoutManager);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                Object object = mCalendarDayAdapter.getObjects().get(i);
                if (object instanceof String) {
                    String value = (String)object;
                    if (!value.isEmpty()) {
                        return 3;
                    }
                }
                if (object instanceof FilterItem) {
                    return 3;
                }
                return 1;
            }
        });
    }

    private void updateUIElements() {
        if (mCalendarItem == null) {
            return;
        }

        Date date = mCalendarItem.getDate();
        Date nowDate = new Date();
        Boolean isFutureDate = true;
        if (DateUtils.isSameDate(date, nowDate) || nowDate.after(date)) {
            isFutureDate = false;
        }

        String dateTitle = StringUtils.capitalizeAll(DateUtils.toString(date, DateUtils.eeeMMMMdd));
        tvDay.setText(dateTitle);

        rlLoggedData.setVisibility(View.GONE);
        llMainBottom.setVisibility(View.GONE);
        llPast.setVisibility(View.GONE);
        llFuture.setVisibility(View.GONE);

        if (isFutureDate) {
            if (mCalendarItem.hasData()) {
                rlLoggedData.setVisibility(View.VISIBLE);
                llMainBottom.setVisibility(View.VISIBLE);
            } else {
                llFuture.setVisibility(View.VISIBLE);
            }
        } else {
            if (mCalendarItem.hasData()) {
                rlLoggedData.setVisibility(View.VISIBLE);
            } else {
                llPast.setVisibility(View.VISIBLE);
            }
        }

        mCalendarDayAdapter.updateObjects(mCalendarItem);

        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
    }

    private void requestCalendarItem() {
        mCalendarManager.getCalendarItem(mCalendarItem.getDate(), new EukiCallback<CalendarItem>() {
            @Override
            public void onSuccess(CalendarItem calendarItem) {
                mCalendarItem = calendarItem;
                updateUIElements();
            }

            @Override
            public void onError(ServerError serverError) {
            }
        });
    }

    @OnClick({R.id.btn_set_reminder, R.id.btn_set_reminder_1})
    void setReminder() {
        startActivity(RemindersActivity.makeIntent(getActivity()));
    }

    @OnClick({R.id.btn_set_appointment, R.id.btn_set_appointment_1})
    void setAppointment() {
        if (mCalendarItem.getAppointments().size() == 0) {
            startActivity(FutureAppointmentActivity.makeIntent(getActivity(), mCalendarItem.getDate()));
        } else {
            startActivity(AppointmentsActivity.makeIntent(getContext(), mCalendarItem.getDate()));
        }
    }

    @OnClick(R.id.btn_log_now)
    void logNow() {
        startActivity(TrackActivity.makeIntent(getContext(), mCalendarItem.getDate(), mCalendarItem));
    }
}
