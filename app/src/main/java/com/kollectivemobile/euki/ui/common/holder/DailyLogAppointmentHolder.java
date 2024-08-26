package com.kollectivemobile.euki.ui.common.holder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.listeners.AppointmentsDataListener;
import com.kollectivemobile.euki.ui.common.listeners.DailyLogViewListener;
import com.kollectivemobile.euki.utils.DateUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyLogAppointmentHolder extends BaseDailyLogHolder {
    public EditText etTitle;
    public EditText etLocation;
    public TextView tvDayTime;
    public TextView tvAlert;

    private List<Integer> mAlertOptions = Arrays.asList(R.string.none,
            R.string.option_30_mins, R.string.option_1_hr, R.string.option_2_hrs, R.string.option_3_hrs,
            R.string.option_1_day, R.string.option_2_day, R.string.option_3_day);
    private WeakReference<Activity> mActivity;

    private AppointmentsDataListener mAppointmentsDataListener;
    private Appointment mAppointment;
    private Date mDate;

    public DailyLogAppointmentHolder(@NonNull View itemView, WeakReference<Activity> activity, DailyLogViewListener listener, AppointmentsDataListener appointmentsDataListener) {
        super(itemView, listener);
        mActivity = activity;
        mAppointmentsDataListener = appointmentsDataListener;
        mAppointment = new Appointment();

        etTitle = itemView.findViewById(R.id.et_title);
        etLocation = itemView.findViewById(R.id.et_location);
        tvDayTime = itemView.findViewById(R.id.tv_day_time);
        tvAlert = itemView.findViewById(R.id.tv_alert);

        itemView.findViewById(R.id.btn_set_future_appointment).setOnClickListener(v -> setFutureAppointment());
        tvDayTime.setOnClickListener(v -> dayTimePressed());
        tvAlert.setOnClickListener(v -> alertPressed());
        itemView.findViewById(R.id.btn_cancel).setOnClickListener(v -> cancelPressed());
        itemView.findViewById(R.id.btn_save).setOnClickListener(v -> savePressed());
    }

    static public DailyLogAppointmentHolder create(ViewGroup parent, WeakReference<Activity> activity, DailyLogViewListener listener, AppointmentsDataListener appointmentsDataListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_appointment, parent, false);
        return new DailyLogAppointmentHolder(view, activity, listener, appointmentsDataListener);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.APPOINTMENT;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasAppointment();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);

        if (selected && mCalendarItem.getAppointments().size() > 0) {
            llContent.setVisibility(View.GONE);
        }
        mDate = calendarItem.getDate();
        clear();
    }

    private void updateUIElements() {
        etTitle.setText(mAppointment.getTitle());
        etLocation.setText(mAppointment.getLocation());
        showDate();
        showAlertOption();
    }

    public Appointment getAppointment() {
        return mAppointment;
    }

    public void showDayTimePicker() {
        final Calendar cal = Calendar.getInstance();
        if (mAppointment.getDate() != null) {
            cal.setTime(mAppointment.getDate());
        }

        DatePickerDialog dpd = new DatePickerDialog(mActivity.get(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.YEAR, year);
                    showTimePicker(cal.getTime());
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void showTimePicker(final Date date) {
        if (mActivity.get() == null) {
            return;
        }

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        TimePickerDialog dpd = new TimePickerDialog(mActivity.get(),
                (timePicker, hours, mins) -> {
                    cal.set(Calendar.HOUR_OF_DAY, hours);
                    cal.set(Calendar.MINUTE, mins);
                    cal.set(Calendar.SECOND, 0);
                    mAppointment.setDate(cal.getTime());
                    showDate();
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        dpd.show();
    }

    public void showAlertOptions() {
        List<Object> objects = new ArrayList<>();
        for (Integer alertRes : mAlertOptions) {
            objects.add(App.getContext().getString(alertRes));
        }

        Dialogs.createListDialog(mActivity.get(), objects, (dialogInterface, i) -> {
            mAppointment.setAlertOption(i);
            showAlertOption();
        }).show();
    }

    private void showDate() {
        if (mAppointment.getDate() == null) {
            tvDayTime.setText(App.getContext().getString(R.string.day_time));
        } else {
            tvDayTime.setText(DateUtils.toString(mAppointment.getDate(), DateUtils.eeeMMMdyyyyhmma));
        }
    }

    private void showAlertOption() {
        if (mAppointment.getAlertOption() == null) {
            tvAlert.setText(App.getContext().getString(R.string.alert));
        } else {
            tvAlert.setText(App.getContext().getString(mAlertOptions.get(mAppointment.getAlertOption())));
        }
    }

    private void setFutureAppointment() {
        if (mAppointmentsDataListener != null) {
            mAppointmentsDataListener.showFutureAppointment();
        }
    }

    private void dayTimePressed() {
        showDayTimePicker();
    }

    private void alertPressed() {
        showAlertOptions();
    }

    private void cancelPressed() {
        clear();
        if (mAppointmentsDataListener != null) {
            mAppointmentsDataListener.cancelAppointment();
        }
    }

    private void savePressed() {
        mAppointment.setTitle(etTitle.getText().toString());
        mAppointment.setLocation(etLocation.getText().toString());

        if (!mAppointment.isDataCompleted()) {
            if (mActivity.get() instanceof BaseActivity) {
                ((BaseActivity) mActivity.get()).showError(App.getContext().getString(R.string.appointment_all_fields));
            }
            return;
        }

        if (mListener != null) {
            mAppointmentsDataListener.saveAppointment(mAppointment);
        }

        clear();
    }

    private void clear() {
        mAppointment = new Appointment();
        mAppointment.setDate(mDate);
        updateUIElements();
    }
}