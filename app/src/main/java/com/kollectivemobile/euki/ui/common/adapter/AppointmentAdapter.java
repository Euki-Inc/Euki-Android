package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.listeners.AppointmentsListener;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentAdapter extends RecyclerView.Adapter implements AppointmentsListener {
    public static final Integer VIEW_TYPE_HEADER = 0;
    public static final Integer VIEW_TYPE_EXISTENT = 1;
    public static final Integer VIEW_TYPE_NEW = 2;

    private List<Integer> mAlertOptions = Arrays.asList(R.string.none,
            R.string.option_30_mins, R.string.option_1_hr, R.string.option_2_hrs, R.string.option_3_hrs,
            R.string.option_1_day, R.string.option_2_day, R.string.option_3_day);

    private Context mContext;
    private AppointmentsDataListener mListener;

    private List<Appointment> mItems;

    private Appointment mCreatedItem;
    private Appointment mEditingItem;

    private Date mDate;
    private CalendarItem mCalendarItem;

    public AppointmentAdapter(Context context, AppointmentsDataListener listener, Date date) {
        mItems = new ArrayList<>();
        mContext = context;
        mListener = listener;
        mDate = date;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.layout_appointments_header, parent, false);
            return new HeaderHolder(view);
        }
        if (viewType == VIEW_TYPE_EXISTENT) {
            View view = inflater.inflate(R.layout.layout_appointments_existent, parent, false);
            return new ExistentHolder(view, this);
        }
        if (viewType == VIEW_TYPE_NEW) {
            View view = inflater.inflate(R.layout.layout_appointments_new, parent, false);
            return new NewHolder(view, this);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Appointment item;
        if (mCreatedItem != null) {
            item = mCreatedItem;
        } else {
            item = mEditingItem;
        }

        if (holder instanceof FieldHolder) {
            FieldHolder fieldHolder = (FieldHolder)holder;
            fieldHolder.llFields.setVisibility(View.GONE);
        }
        if (holder instanceof ExistentHolder) {
            ExistentHolder existentHolder = (ExistentHolder)holder;
            existentHolder.tvTitle.setText(mItems.get(position - 1).getTitle());
        }

        if (position == 0) {
            return;
        }
        if (position < mItems.size() + 1) {
            if (mEditingItem == null) {
                return;
            }
            if (!mItems.get(position - 1).getId().equals(mEditingItem.getId())) {
                return;
            }
        }
        if (position == mItems.size() + 1 && mCreatedItem == null) {
            return;
        }
        if (item == null) {
            return;
        }

        if (holder instanceof FieldHolder) {
            FieldHolder fieldHolder = (FieldHolder)holder;
            fieldHolder.llFields.setVisibility(View.VISIBLE);
            fieldHolder.etTitle.setText(item.getTitle());
            fieldHolder.etLocation.setText(item.getLocation());

            if (item.getTitle() != null) {
                fieldHolder.etTitle.setSelection(item.getTitle().length());
            }
            if (item.getLocation() != null) {
                fieldHolder.etLocation.setSelection(item.getLocation().length());
            }

            fieldHolder.etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    item.setTitle(editable.toString());
                }
            });

            fieldHolder.etLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    item.setLocation(editable.toString());
                }
            });

            if (item.getDate() == null) {
                fieldHolder.tvDayTime.setText(mContext.getString(R.string.day_time));
            } else {
                fieldHolder.tvDayTime.setText(DateUtils.toString(item.getDate(), DateUtils.eeeMMMdyyyyhmma));
            }
            if (item.getAlertOption() == null) {
                fieldHolder.tvAlert.setText(mContext.getString(R.string.alert));
            } else {
                if (item.getAlertOption() == 0) {
                    fieldHolder.tvAlert.setText(mContext.getString(R.string.none));
                } else {
                    fieldHolder.tvAlert.setText(App.getContext().getString(mAlertOptions.get(item.getAlertOption())));
                }
            }
            fieldHolder.btnAdd.setText(mContext.getString(item.getId() == null ? R.string.add : R.string.save));
        }

        if (holder instanceof ExistentHolder) {
            ExistentHolder existentHolder = (ExistentHolder)holder;
            existentHolder.tvTitle.setText(item.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        if (position <= mItems.size()) {
            return VIEW_TYPE_EXISTENT;
        }
        return VIEW_TYPE_NEW;
    }

    @Override
    public void newSelected() {
        mCreatedItem = (mCreatedItem == null) ? new Appointment(mDate) : null;
        notifyDataSetChanged();
    }

    @Override
    public void dayTimeSelected() {
        if (mCreatedItem != null) {
            mListener.dayTimeSelected(mCreatedItem.getDate());
        }
        if (mEditingItem != null) {
            mListener.dayTimeSelected(mEditingItem.getDate());
        }
    }

    @Override
    public void alertSelected() {
        mListener.alertSelected();
    }

    @Override
    public void appointmentSelected(int position) {
        mCreatedItem = null;
        mEditingItem = mEditingItem == null ? mItems.get(position).copy() : null;
        notifyDataSetChanged();
    }

    @Override
    public void saveSelected() {
        Appointment editedItem;
        if (mCreatedItem != null) {
            editedItem = mCreatedItem;
        } else {
            editedItem = mEditingItem;
        }

        if (editedItem == null) {
            return;
        }

        if (mListener != null) {
            mListener.saveAppointment(editedItem);
        }
    }

    @Override
    public void cancelSelected() {
        clear();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ExistentHolder extends FieldHolder {
        public @BindView(R.id.tv_title) TextView tvTitle;

        public ExistentHolder(@NonNull View itemView, AppointmentsListener listener) {
            super(itemView, listener);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_title)
        void onClick() {
            if (mListener != null) {
                mListener.appointmentSelected(getLayoutPosition() - 1);
            }
        }
    }

    public class NewHolder extends FieldHolder {
        public NewHolder(@NonNull View itemView, AppointmentsListener listener) {
            super(itemView, listener);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_new)
        void onClick() {
            if (mListener != null) {
                mListener.newSelected();
            }
        }
    }

    public class FieldHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.et_title) EditText etTitle;
        public @BindView(R.id.et_location) EditText etLocation;
        public @BindView(R.id.tv_day_time) TextView tvDayTime;
        public @BindView(R.id.tv_alert) TextView tvAlert;
        public @BindView(R.id.btn_add) Button btnAdd;
        public @BindView(R.id.ll_fields) LinearLayout llFields;
        protected AppointmentsListener mListener;

        public FieldHolder(@NonNull View itemView, AppointmentsListener listener) {
            super(itemView);
            mListener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_day_time)
        void dayTimePressed() {
            if (mListener != null) {
                mListener.dayTimeSelected();
            }
        }

        @OnClick(R.id.tv_alert)
        void alertPressed() {
            if (mListener != null) {
                mListener.alertSelected();
            }
        }

        @OnClick(R.id.btn_cancel)
        void cancelPressed() {
            if (mListener != null) {
                mListener.cancelSelected();
            }
        }

        @OnClick(R.id.btn_add)
        void addPressed() {
            if (mListener != null) {
                mListener.saveSelected();
            }
        }
    }

    public void update(CalendarItem calendarItem) {
        if (calendarItem != null) {
            mCalendarItem = calendarItem;
            mItems.clear();
            mItems.addAll(calendarItem.getAppointments());
            clear();
        }
    }

    public void clear() {
        mCreatedItem = null;
        mEditingItem = null;
        notifyDataSetChanged();
    }

    public void selectDate(Date date) {
        if (mCreatedItem != null) {
            mCreatedItem.setDate(date);
        }
        if (mEditingItem != null) {
            mEditingItem.setDate(date);
        }
        notifyDataSetChanged();
    }

    public void selectAlert(Integer alert) {
        if (mCreatedItem != null) {
            mCreatedItem.setAlertOption(alert);
        }
        if (mEditingItem != null) {
            mEditingItem.setAlertOption(alert);
        }
        notifyDataSetChanged();
    }

    public interface AppointmentsDataListener {
        void saveAppointment(Appointment appointment);
        void dayTimeSelected(Date date);
        void alertSelected();
    }
}
