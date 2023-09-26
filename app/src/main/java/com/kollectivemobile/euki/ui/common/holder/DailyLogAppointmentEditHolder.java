package com.kollectivemobile.euki.ui.common.holder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.ui.common.BaseActivity;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.listeners.AppointmentsDataListener;
import com.kollectivemobile.euki.utils.DateUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyLogAppointmentEditHolder extends RecyclerView.ViewHolder {
    public @BindView(R.id.et_title) EditText etTitle;
    public @BindView(R.id.et_location) EditText etLocation;
    public @BindView(R.id.tv_day_time) TextView tvDayTime;
    public @BindView(R.id.tv_alert) TextView tvAlert;

    private List<Integer> mAlertOptions = Arrays.asList(R.string.none,
            R.string.option_30_mins, R.string.option_1_hr, R.string.option_2_hrs, R.string.option_3_hrs,
            R.string.option_1_day, R.string.option_2_day, R.string.option_3_day);
    private WeakReference<Activity> mActivity;
    private AppointmentsDataListener mListener;
    private Appointment mAppointment;

    public DailyLogAppointmentEditHolder(@NonNull View itemView, Activity activity, AppointmentsDataListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mActivity = new WeakReference<>(activity);
        mListener = listener;
    }

    static public DailyLogAppointmentEditHolder create(Activity activity, ViewGroup parent, AppointmentsDataListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_appointment_edit, parent, false);
        return new DailyLogAppointmentEditHolder(view, activity, listener);
    }

    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.APPOINTMENTEDIT;
    }

    public void bind(Appointment appointment) {
        mAppointment = appointment;
        etTitle.setText(appointment.getTitle());
        etLocation.setText(appointment.getLocation());
        showDate();
        showAlertOption();
    }

    public void showDayTimePicker() {
        final Calendar cal = Calendar.getInstance();
        if (mAppointment.getDate() != null) {
            cal.setTime(mAppointment.getDate());
        }

        DatePickerDialog dpd = new DatePickerDialog(mActivity.get(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.YEAR, year);
                        showTimePicker(cal.getTime());
                    }
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
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                        cal.set(Calendar.HOUR_OF_DAY, hours);
                        cal.set(Calendar.MINUTE, mins);
                        cal.set(Calendar.SECOND, 0);
                        mAppointment.setDate(cal.getTime());
                        showDate();
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        dpd.show();
    }

    public void showAlertOptions() {
        List<Object> objects = new ArrayList<>();
        for (Integer alertRes : mAlertOptions) {
            objects.add(App.getContext().getString(alertRes));
        }

        Dialogs.createListDialog(mActivity.get(), objects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAppointment.setAlertOption(i);
                showAlertOption();
            }
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

    @OnClick(R.id.tv_day_time)
    void dayTimePressed() {
        showDayTimePicker();
    }

    @OnClick(R.id.tv_alert)
    void alertPressed() {
        showAlertOptions();
    }

    @OnClick(R.id.btn_cancel)
    void cancelPressed() {
        if (mListener != null) {
            mListener.cancelAppointment();
        }
    }

    @OnClick(R.id.btn_save)
    void savePressed() {
        Appointment appointment = mAppointment.copy();
        appointment.setTitle(etTitle.getText().toString());
        appointment.setLocation(etLocation.getText().toString());

        if (!appointment.isDataCompleted()) {
            if (mActivity.get() instanceof BaseActivity) {
                ((BaseActivity)mActivity.get()).showError(App.getContext().getString(R.string.appointment_all_fields));
            }
            return;
        }

        if (mListener != null) {
            mAppointment.setTitle(etTitle.getText().toString());
            mAppointment.setLocation(etLocation.getText().toString());
            mListener.saveAppointment(mAppointment);
        }
    }
}
