package com.kollectivemobile.euki.ui.calendar.appointments;

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
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.manager.ReminderManager;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.model.database.entity.ReminderItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.InsetDecoration;
import com.kollectivemobile.euki.ui.common.SwipeController;
import com.kollectivemobile.euki.ui.common.SwipeControllerActions;
import com.kollectivemobile.euki.ui.common.adapter.AppointmentAdapter;
import com.kollectivemobile.euki.ui.common.adapter.ReminderAdapter;
import com.kollectivemobile.euki.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AppointmentsFragment extends BaseFragment implements AppointmentAdapter.AppointmentsDataListener {
    @Inject CalendarManager mCalendarManager;

    @BindView(R.id.rv_main) RecyclerView rvMain;

    private Date mDate;
    private CalendarItem mCalendarItem;
    private AppointmentAdapter mAdapter;
    private SwipeController swipeController = null;

    public static AppointmentsFragment newInstance(Date date) {
        Bundle args = new Bundle();
        AppointmentsFragment fragment = new AppointmentsFragment();
        fragment.mDate = date;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
        setUIElements();
        requestCalendarItem();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments, container, false);
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
        mAdapter = new AppointmentAdapter(getContext(), this, mDate);
        rvMain.setAdapter(mAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMain.addItemDecoration(new InsetDecoration(getContext(), InsetDecoration.VERTICAL_LIST));

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.delete_appointment), getString(R.string.delete), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Appointment appointment = mCalendarItem.getAppointments().get(position - 1);
                        mCalendarItem.getAppointments().remove(appointment);

                        //TODO: Implement logic to remove appointment

                        mCalendarManager.saveItem(mCalendarItem, new EukiCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                dismissProgressDialog();
                                mAdapter.clear();
                                requestCalendarItem();
                            }

                            @Override
                            public void onError(ServerError serverError) {
                                dismissProgressDialog();
                                showError(serverError.getMessage());
                            }
                        });
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

    private void requestCalendarItem() {
        mCalendarManager.getCalendarItem(mDate, new EukiCallback<CalendarItem>() {
            @Override
            public void onSuccess(CalendarItem calendarItem) {
                mCalendarItem = calendarItem;
                mAdapter.update(calendarItem);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        });
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        if (!appointment.isDataCompleted()) {
            showError(getString(R.string.appointment_all_fields));
            return;
        }

        for (int i=0; i<mCalendarItem.getAppointments().size(); i++) {
            Appointment existentAppintment = mCalendarItem.getAppointments().get(i);
            if (existentAppintment.getId().equals(appointment.getId())) {
                mCalendarItem.getAppointments().set(i, appointment);
                break;
            }
        }

        if (!mCalendarItem.getAppointments().contains(appointment)) {
            mCalendarItem.getAppointments().add(appointment);
        }

        showProgressDialog();
        mCalendarManager.saveItem(mCalendarItem, new EukiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                dismissProgressDialog();
                mAdapter.clear();
                requestCalendarItem();
            }

            @Override
            public void onError(ServerError serverError) {
                dismissProgressDialog();
                showError(serverError.getMessage());
            }
        });
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
    public void alertSelected() {
        List<Object> objects = new ArrayList<>();
        for (Integer alertRes : Constants.sAlertOptions) {
            objects.add(App.getContext().getString(alertRes));
        }

        Dialogs.createListDialog(getActivity(), objects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAdapter.selectAlert(i);
            }
        }).show();
    }
}
