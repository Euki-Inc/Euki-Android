package com.kollectivemobile.euki.ui.calendar.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.ReminderManager;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.InsetDecoration;
import com.kollectivemobile.euki.ui.common.SwipeController;
import com.kollectivemobile.euki.ui.common.SwipeControllerActions;
import com.kollectivemobile.euki.ui.common.adapter.ReminderAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RemindersFragment extends BaseFragment implements ReminderAdapter.RemindersDataListener {
    @Inject ReminderManager mReminderManager;

    @BindView(R.id.rv_main) RecyclerView rvMain;

    private List<ReminderItem> mReminderItems;
    private ReminderAdapter mAdapter;
    private SwipeController swipeController = null;

    public static RemindersFragment newInstance() {
        Bundle args = new Bundle();
        RemindersFragment fragment = new RemindersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
        setUIElements();
        requestReminders();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_done:
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUIElements() {
        mReminderItems = new ArrayList<>();
        mAdapter = new ReminderAdapter(getContext(), this);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMain.addItemDecoration(new InsetDecoration(getContext(), InsetDecoration.VERTICAL_LIST));

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.delete_reminder), getString(R.string.delete), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReminderItem reminderItem = mReminderItems.get(position - 2);
                        mReminderManager.removeReminder(reminderItem);
                        requestReminders();
                    }
                }).show();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvMain);

        rvMain.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void requestReminders() {
        mReminderManager.getReminders(new EukiCallback<List<ReminderItem>>() {
            @Override
            public void onSuccess(List<ReminderItem> reminderItems) {
                mReminderItems.clear();
                mReminderItems.addAll(reminderItems);
                mAdapter.update(mReminderItems);
            }

            @Override
            public void onError(ServerError serverError) {
                mReminderItems.clear();
                mAdapter.update(mReminderItems);
            }
        });
    }

    @Override
    public void saveReminder(ReminderItem reminderItem) {
        if (reminderItem.getTitle() == null || reminderItem.getTitle().isEmpty() ||
            reminderItem.getDate() == null) {
            showError(getString(R.string.reminder_all_fields));
            return;
        }

        if (reminderItem.getRepeatDays() == null) {
            reminderItem.setRepeatDays(0);
        }

        if (reminderItem.getId() == 0) {
            mReminderManager.addReminder(reminderItem);
        } else {
            mReminderManager.updateReminder(reminderItem);
        }

        mAdapter.clear();
        requestReminders();
    }

    @Override
    public void dayTimeSelected(Date date) {
        final Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.YEAR, year);
                        showTimePicker(cal.getTime());
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMinDate(new Date().getTime());
        dpd.show();
    }

    private void showTimePicker(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        TimePickerDialog dpd = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                        cal.set(Calendar.HOUR_OF_DAY, hours);
                        cal.set(Calendar.MINUTE, mins);
                        cal.set(Calendar.SECOND, 0);
                        mAdapter.selectDate(cal.getTime());
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        dpd.show();
    }

    @Override
    public void repeatSelected() {
        List<Object> objects = new ArrayList<>();
        objects.add(getString(R.string.none));

        for (int i=1; i<31; i++) {
            objects.add(i + " " + getString(R.string.days));
        }

        Dialogs.createListDialog(getActivity(), objects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAdapter.selectRepeat(i);
            }
        }).show();
    }
}
