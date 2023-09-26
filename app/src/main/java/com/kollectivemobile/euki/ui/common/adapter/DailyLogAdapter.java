package com.kollectivemobile.euki.ui.common.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.Dialogs;
import com.kollectivemobile.euki.ui.common.holder.DailyLogAppointmentEditHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogAppointmentExistentHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogAppointmentHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogAppointmentNewHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogBleedingHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogBodyHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogContraceptionHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogEmotionsHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogNoteHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogSexualActivityHolder;
import com.kollectivemobile.euki.ui.common.holder.DailyLogTestHolder;
import com.kollectivemobile.euki.ui.common.listeners.AppointmentsDataListener;
import com.kollectivemobile.euki.ui.common.listeners.DailyLogViewListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DailyLogAdapter extends RecyclerView.Adapter implements DailyLogViewListener, AppointmentsDataListener, DailyLogAppointmentExistentHolder.AppointmentExistentListener {
    public enum ViewType {
        BLEEDING, EMOTIONS, BODY, SEXUALACTIVITY, CONTRACEPTION, TEST, NOTE, APPOINTMENT, APPOINTMENTEDIT, APPOINTMENTEXISTENT, APPOINTMENTNEW;
    }

    private List<FilterItem> mFilterItems;
    private CalendarItem mCalendarItem;
    private DailyLogListener mListener;
    private WeakReference<Context> mContext;
    private WeakReference<Activity> mActivity;
    private ViewType mSelectedViewType;

    private WeakReference<DailyLogAppointmentHolder> mDailyLogAppointmentHolder;

    private int mAppointmentPosition = -1;
    private int mAppointmentCells = -1;
    private int mAppointmentSelected = -1;
    private boolean mNewAppointmentSelected = false;
    private boolean mBleedingTrackingEnabled;

    public DailyLogAdapter(Activity activity, Context context, DailyLogListener listener, Boolean bleedingTrackingEnabled) {
        mFilterItems = new ArrayList<>();
        mCalendarItem = new CalendarItem();
        mContext = new WeakReference<>(context);
        mActivity = new WeakReference<>(activity);
        mListener = listener;
        mBleedingTrackingEnabled = bleedingTrackingEnabled;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.BLEEDING.ordinal()) {
            return DailyLogBleedingHolder.create(parent, this, mBleedingTrackingEnabled);
        }
        if (viewType == ViewType.EMOTIONS.ordinal()) {
            return DailyLogEmotionsHolder.create(parent, this);
        }
        if (viewType == ViewType.BODY.ordinal()) {
            return DailyLogBodyHolder.create(parent, this);
        }
        if (viewType == ViewType.SEXUALACTIVITY.ordinal()) {
            return DailyLogSexualActivityHolder.create(parent, this);
        }
        if (viewType == ViewType.CONTRACEPTION.ordinal()) {
            return DailyLogContraceptionHolder.create(parent, this);
        }
        if (viewType == ViewType.TEST.ordinal()) {
            return DailyLogTestHolder.create(parent, this);
        }
        if (viewType == ViewType.NOTE.ordinal()) {
            return DailyLogNoteHolder.create(parent, this);
        }
        if (viewType == ViewType.APPOINTMENT.ordinal()) {
            return DailyLogAppointmentHolder.create(parent, mActivity, this, this);
        }
        if (viewType == ViewType.APPOINTMENTEXISTENT.ordinal()) {
            return DailyLogAppointmentExistentHolder.create(parent, this);
        }
        if (viewType == ViewType.APPOINTMENTEDIT.ordinal()) {
            return DailyLogAppointmentEditHolder.create(mActivity.get(), parent, this);
        }
        if (viewType == ViewType.APPOINTMENTNEW.ordinal()) {
            return DailyLogAppointmentNewHolder.create(parent, mActivity, this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DailyLogBleedingHolder) {
            DailyLogBleedingHolder dailyHolder = (DailyLogBleedingHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.BLEEDING), mSelectedViewType);
        } else if (holder instanceof DailyLogEmotionsHolder) {
            DailyLogEmotionsHolder dailyHolder = (DailyLogEmotionsHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.EMOTIONS), mSelectedViewType);
        } else if (holder instanceof DailyLogBodyHolder) {
            DailyLogBodyHolder dailyHolder = (DailyLogBodyHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.BODY), mSelectedViewType);
        } else if (holder instanceof DailyLogSexualActivityHolder) {
            DailyLogSexualActivityHolder dailyHolder = (DailyLogSexualActivityHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.SEXUALACTIVITY), mSelectedViewType);
        } else if (holder instanceof DailyLogContraceptionHolder) {
            DailyLogContraceptionHolder dailyHolder = (DailyLogContraceptionHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.CONTRACEPTION), mSelectedViewType);
        } else if (holder instanceof DailyLogTestHolder) {
            DailyLogTestHolder dailyHolder = (DailyLogTestHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.TEST), mSelectedViewType);
        } else if (holder instanceof DailyLogNoteHolder) {
            DailyLogNoteHolder dailyHolder = (DailyLogNoteHolder)holder;
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.NOTE), mSelectedViewType);
        } else if (holder instanceof DailyLogAppointmentHolder) {
            DailyLogAppointmentHolder dailyHolder = (DailyLogAppointmentHolder)holder;
            mDailyLogAppointmentHolder = new WeakReference<>(dailyHolder);
            dailyHolder.bind(mCalendarItem, mSelectedViewType == null ? false : (mSelectedViewType == ViewType.APPOINTMENT), mSelectedViewType);
        } else if (holder instanceof DailyLogAppointmentNewHolder) {
            DailyLogAppointmentNewHolder dailyHolder = (DailyLogAppointmentNewHolder)holder;
            dailyHolder.bind(mCalendarItem, mNewAppointmentSelected);
        } else if (holder instanceof DailyLogAppointmentEditHolder) {
            DailyLogAppointmentEditHolder dailyHolder = (DailyLogAppointmentEditHolder)holder;
            int fixedPosition = position - mAppointmentPosition - 1;
            dailyHolder.bind(mCalendarItem.getAppointments().get(fixedPosition));
        } else if (holder instanceof DailyLogAppointmentExistentHolder) {
            DailyLogAppointmentExistentHolder dailyHolder = (DailyLogAppointmentExistentHolder)holder;
            int fixedPosition = position - mAppointmentPosition - 1;
            dailyHolder.bind(mCalendarItem.getAppointments().get(fixedPosition));
        }
    }

    @Override
    public int getItemCount() {
        mAppointmentPosition = -1;
        Integer count = 0;
        for (FilterItem filterItem : mFilterItems) {
            count++;
            switch (filterItem.getTitle()) {
                case "appointment":
                    mAppointmentPosition = count - 1;
                    mAppointmentCells = 1;

                    if (mSelectedViewType != null && mSelectedViewType == ViewType.APPOINTMENT) {
                        if (mCalendarItem.getAppointments().size() > 0) {
                            count += mCalendarItem.getAppointments().size() + 1;
                            mAppointmentCells += mCalendarItem.getAppointments().size() + 1;
                        }
                    }
                    break;
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (mAppointmentPosition == -1) {
            return getViewType(mFilterItems.get(position)).ordinal();
        }

        if (position <= mAppointmentPosition) {
            return getViewType(mFilterItems.get(position)).ordinal();
        }
        if (mAppointmentPosition < position && position < mAppointmentPosition + mAppointmentCells) {
            int fixedPosition = position - mAppointmentPosition;

            if (fixedPosition == mAppointmentCells - 1) {
                return ViewType.APPOINTMENTNEW.ordinal();
            } else {
                return (mAppointmentSelected == (fixedPosition - 1) ? ViewType.APPOINTMENTEDIT : ViewType.APPOINTMENTEXISTENT).ordinal();
            }
        }
        int fixedPosition = position - (mAppointmentCells - 1);
        return getViewType(mFilterItems.get(fixedPosition)).ordinal();
    }

    private ViewType getViewType(FilterItem filterItem) {
        switch (filterItem.getTitle()) {
            case "bleeding":
                return ViewType.BLEEDING;
            case "emotions":
                return ViewType.EMOTIONS;
            case "body":
                return ViewType.BODY;
            case "sexual_activity":
                return ViewType.SEXUALACTIVITY;
            case "contraception":
                return ViewType.CONTRACEPTION;
            case "test":
                return ViewType.TEST;
            case "appointment":
                return ViewType.APPOINTMENT;
            case "note":
                return ViewType.NOTE;
        }
        return null;
    }

    public void update(CalendarItem calendarItem, List<FilterItem> items) {
        if (calendarItem != null) {
            mCalendarItem = calendarItem;

            List<FilterItem> filterItems = new ArrayList<>();
            for (FilterItem item : items) {
                if (item.getOn()) {
                    filterItems.add(item);
                }
            }
            mFilterItems = filterItems;

            notifyDataSetChanged();
        }
    }

    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        if (mCalendarItem.getAppointments().size() == 0) {
            if (mDailyLogAppointmentHolder != null && mDailyLogAppointmentHolder.get() != null) {
                Appointment appointment = mDailyLogAppointmentHolder.get().getAppointment();
                if (appointment != null && appointment.isDataCompleted()) {
                    appointments.add(appointment);
                }
            }
        } else {
            appointments.addAll(mCalendarItem.getAppointments());
        }

        return appointments;
    }

    /**
       DailyLogViewListener Methods
     */

    @Override
    public void selectedViewType(ViewType type, Integer index) {
        mAppointmentSelected = -1;
        mNewAppointmentSelected = false;

        if (mSelectedViewType == type) {
            mSelectedViewType = null;
        } else {
            mSelectedViewType = type;
        }

        if (mSelectedViewType == ViewType.BLEEDING) {
            mListener.bleedingSelected();
        }

        if (mSelectedViewType != null) {
            mListener.categoryOpened(index);
        }

        notifyDataSetChanged();
    }

    @Override
    public void categorySelected(Integer index, Integer offset) {
        if (mSelectedViewType != null) {
            mListener.categoryOpened(index, offset);
        }
    }

    /**
     * AppointmentsDataListener Methods
     */

    @Override
    public void saveAppointment(Appointment appointment) {
        if (!mCalendarItem.getAppointments().contains(appointment)) {
            mCalendarItem.getAppointments().add(appointment);
        }

        mAppointmentSelected = -1;
        mNewAppointmentSelected = false;
        notifyDataSetChanged();
    }

    @Override
    public void cancelAppointment() {
        mAppointmentSelected = -1;
        mNewAppointmentSelected = false;
        notifyDataSetChanged();
    }

    @Override
    public void newAppointmentSelected() {
        mAppointmentSelected = -1;
        mNewAppointmentSelected = !mNewAppointmentSelected;
        notifyDataSetChanged();
    }

    @Override
    public void showFutureAppointment() {
        mListener.futureAppointmentSelected();
    }

    /**
     * DailyLogAppointmentExistentHolder.AppointmentExistentListener Methods
     */

    @Override
    public void selectedAppointment(Appointment appointment) {
        mNewAppointmentSelected = false;
        int indexSelected = mCalendarItem.getAppointments().indexOf(appointment);

        if (indexSelected == mAppointmentSelected) {
            mAppointmentSelected = -1;
        } else {
            mAppointmentSelected = indexSelected;
        }

        notifyDataSetChanged();
    }

    @Override
    public void dataChanged() {
        mListener.dataChanged();
    }

    @Override
    public void infoAction() {
        Dialogs.createSimpleDialog(mContext.get(), null, App.getContext().getString(R.string.bleeding_info), null).show();
    }

    @Override
    public void checkIncludeBleedingCycle() {
        mListener.checkIncludeBleedingCycle();
    }

    public interface DailyLogListener {
        void futureAppointmentSelected();
        void bleedingSelected();
        void categoryOpened(Integer index);
        void categoryOpened(Integer index, Integer offset);
        void dataChanged();
        void checkIncludeBleedingCycle();
    }
}
