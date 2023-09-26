package com.kollectivemobile.euki.ui.calendar.appointments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.CalendarManager;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.utils.Constants;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class FutureAppointmentFragment extends BaseFragment {
    @Inject CalendarManager mCalendarManager;

    public @BindView(R.id.et_title) EditText etTitle;
    public @BindView(R.id.et_location) EditText etLocation;
    public @BindView(R.id.tv_day_time) TextView tvDayTime;
    public @BindView(R.id.tv_alert) TextView tvAlert;
    public @BindView(R.id.btn_add) Button btnAdd;

    private Date mSelectedDate;
    private Integer mSelectedAlertOption;

    public static FutureAppointmentFragment newInstance(Date date) {
        Bundle args = new Bundle();
        FutureAppointmentFragment fragment = new FutureAppointmentFragment();
        fragment.mSelectedDate = date;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_future_appointment, container, false);
    }

    private void setUIElements() {
        updateUIElements();
    }

    private void updateUIElements() {
        if (mSelectedDate == null) {
            tvDayTime.setText(getString(R.string.day_time));
        } else {
            tvDayTime.setText(DateUtils.toString(mSelectedDate, DateUtils.eeeMMMdyyyyhmma));
        }
        if (mSelectedAlertOption == null) {
            tvAlert.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
            tvAlert.setText(getString(R.string.alert));
        } else {
            tvAlert.setTextColor(ContextCompat.getColor(getContext(), R.color.euki_main));
            if (mSelectedAlertOption == 0) {
                tvAlert.setText(getString(R.string.none));
            } else {
                tvAlert.setText(App.getContext().getString(Constants.sAlertOptions.get(mSelectedAlertOption)));
            }
        }
    }

    private void dayTimeSelected() {
        final Calendar cal = Calendar.getInstance();
        if (mSelectedDate != null) {
            cal.setTime(mSelectedDate);
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
                        mSelectedDate = cal.getTime();
                        updateUIElements();
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        dpd.show();
    }

    private void saveAppointment() {
        Appointment appointment = new Appointment();
        appointment.setTitle(etTitle.getText().toString());
        appointment.setLocation(etLocation.getText().toString());
        appointment.setDate(mSelectedDate);
        appointment.setAlertOption(mSelectedAlertOption == null ? 0 : mSelectedAlertOption);

        if (!appointment.isDataCompleted()) {
            showError(getString(R.string.appointment_all_fields));
            return;
        }

        showProgressDialog();
        mCalendarManager.save(appointment, new EukiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                dismissProgressDialog();

                Dialogs.createSimpleDialog(getActivity(), null, getString(R.string.future_appointment_confirmation), getString(R.string.ok), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).show();
            }

            @Override
            public void onError(ServerError serverError) {
                dismissProgressDialog();
                showError(serverError.getMessage());
            }
        });
    }

    @OnClick(R.id.tv_day_time)
    void dayTimePressed() {
        dayTimeSelected();
    }

    @OnClick(R.id.tv_alert)
    void alertPressed() {
        List<Object> objects = new ArrayList<>();
        for (Integer alertRes : Constants.sAlertOptions) {
            objects.add(App.getContext().getString(alertRes));
        }

        Dialogs.createListDialog(getActivity(), objects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSelectedAlertOption = i;
                updateUIElements();
            }
        }).show();
    }

    @OnClick(R.id.btn_cancel)
    void cancelPressed() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_add)
    void addPressed() {
        saveAppointment();
    }
}
