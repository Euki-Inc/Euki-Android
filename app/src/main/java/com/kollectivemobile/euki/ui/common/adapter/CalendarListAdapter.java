package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Appointment;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.utils.Constants;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.Utils;
import com.kollectivemobile.euki.utils.strings.StringUtils;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarListAdapter extends RecyclerView.Adapter {
    private CalendarFilter mCalendarFilter;
    private List<CalendarItem> mCalendarItems;
    private CalendarListListener mListener;
    private Context mContext;

    public CalendarListAdapter(Context context, CalendarListListener listener) {
        mCalendarFilter = new CalendarFilter();
        mCalendarItems = new ArrayList<>();
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_calendar_list, parent, false);
        return new CalendarListHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CalendarListHolder rowHolder = (CalendarListHolder) holder;
        CalendarItem calendarItem = mCalendarItems.get(position);
        String title = StringUtils.capitalizeAll(DateUtils.toString(calendarItem.getDate(), DateUtils.eeeMMMMdd));

        rowHolder.tvTitle.setText(title);

        rowHolder.llContainer.removeAllViews();

        if (mCalendarFilter.getBleedingOn() || mCalendarFilter.showAll()) {
            List<String> items = new ArrayList();

            if (calendarItem.getBleedingSize() != null) {
                items.add(calendarItem.getBleedingSize().getText());
            }

            for (int i=0; i<calendarItem.getBleedingClotsCounter().size(); i++) {
                Integer value = calendarItem.getBleedingClotsCounter().get(i);
                if (value > 0) {
                    items.add(countValue(value, (i == 0) ? "bleeding_clots_1" : "bleeding_clots_2"));
                }
            }

            for (int i=0; i<calendarItem.getBleedingProductsCounter().size(); i++) {
                Integer value = calendarItem.getBleedingProductsCounter().get(i);
                if (value > 0) {
                    items.add(countValue(value, "bleeding_produc_" + (i + 1)));
                }
            }

            if (items.size() > 0) {
                rowHolder.llContainer.addView(getDotTitleView(convert(items), R.color.bleeding));
            }
        }

        if (mCalendarFilter.getEmotionsOn() || mCalendarFilter.showAll()) {
            List<String> items = new ArrayList<>();
            for (Constants.Emotions emotion : calendarItem.getEmotions()) {
                items.add(emotion.getText());
            }
            if (items.size() > 0) {
                rowHolder.llContainer.addView(getDotTitleView(convert(items), R.color.emotions));
            }
        }

        if (mCalendarFilter.getBodyOn() || mCalendarFilter.showAll()) {
            List<String> items = new ArrayList<>();
            for (Constants.Body body : calendarItem.getBody()) {
                items.add(body.getText());
            }
            if (items.size() > 0) {
                rowHolder.llContainer.addView(getDotTitleView(convert(items), R.color.body));
            }
        }
        if (mCalendarFilter.getSexualActivityOn() || mCalendarFilter.showAll()) {
            List<String> items = new ArrayList<>();
            for (int i=0; i<calendarItem.getSexualProtectionSTICounter().size(); i++) {
                Integer value = calendarItem.getSexualProtectionSTICounter().get(i);
                if (value > 0) {
                    items.add(countValue(value, "protection_sti_" + (i + 1) + "_list"));
                }
            }
            for (int i=0; i<calendarItem.getSexualProtectionPregnancyCounter().size(); i++) {
                Integer value = calendarItem.getSexualProtectionPregnancyCounter().get(i);
                if (value > 0) {
                    items.add(countValue(value, "protection_pregnancy_" + (i + 1) + "_list"));
                }
            }
            for (int i=0; i<calendarItem.getSexualOtherCounter().size(); i++) {
                Integer value = calendarItem.getSexualOtherCounter().get(i);
                if (value > 0) {
                    items.add(countValue(value, "protection_other_" + (i + 1)));
                }
            }
            if (items.size() > 0) {
                rowHolder.llContainer.addView(getDotTitleView(convert(items), R.color.sexual_activity));
            }
        }
        if (mCalendarFilter.getContraceptionOn() || mCalendarFilter.showAll()) {
            List<String> items = new ArrayList<>();
            if (calendarItem.getContraceptionPills() != null) {
                items.add(calendarItem.getContraceptionPills().getText() + "_list");
            }
            for (Constants.ContraceptionDailyOther contraceptionDailyOther : calendarItem.getContraceptionDailyOther()) {
                items.add(contraceptionDailyOther.getText());
            }
            if (calendarItem.getContraceptionIUD() != null) {
                items.add(calendarItem.getContraceptionIUD().getText() + "_list");
            }
            if (calendarItem.getContraceptionImplant() != null) {
                items.add(calendarItem.getContraceptionImplant().getText() + "_list");
            }
            if (calendarItem.getContraceptionPatch() != null) {
                items.add(calendarItem.getContraceptionPatch().getText() + "_list");
            }
            if (calendarItem.getContraceptionRing() != null) {
                items.add(calendarItem.getContraceptionRing().getText() + "_list");
            }
            for (Constants.ContraceptionLongTermOther contraceptionLongTermOther : calendarItem.getContraceptionLongTermOthers()) {
                items.add(contraceptionLongTermOther.getText());
            }
            if (items.size() > 0) {
                rowHolder.llContainer.addView(getDotTitleView(convert(items), R.color.contraception));
            }
        }
        if (mCalendarFilter.getTestOn() || mCalendarFilter.showAll()) {
            List<String> items = new ArrayList<>();
            if (calendarItem.getTestSTI() != null) {
                items.add(calendarItem.getTestSTI().getText() + "_list");
            }
            if (calendarItem.getTestPregnancy() != null) {
                items.add(calendarItem.getTestPregnancy().getText() + "_list");
            }
            if (items.size() > 0) {
                rowHolder.llContainer.addView(getDotTitleView(convert(items), R.color.test));
            }
        }
        if (calendarItem.getAppointments().size() > 0 && (mCalendarFilter.getAppointmentOn() || mCalendarFilter.showAll())) {
            rowHolder.llContainer.addView(getDotTitleView("appointment", R.color.appointment));
            for (Appointment appointment : calendarItem.getAppointments()) {
                StringBuffer sb = new StringBuffer();
                if (appointment.getTitle() != null) {
                    sb.append(appointment.getTitle());
                }
                if (appointment.getLocation() != null) {
                    sb.append(" - ");
                    sb.append(appointment.getLocation());
                }
                if (appointment.getDate() != null) {
                    sb.append(" ");
                    sb.append(DateUtils.toString(appointment.getDate(), DateUtils.eeeMMMdyyyyhmma));
                }
                rowHolder.llContainer.addView(getDotTitleView(sb.toString(), R.color.white));
            }
        }
        if (calendarItem.getNote() != null && !calendarItem.getNote().isEmpty() && (mCalendarFilter.getNoteOn() || mCalendarFilter.showAll())) {
            rowHolder.llContainer.addView(getDotTitleView(calendarItem.getNote(), R.color.note));
        }
    }

    private String countValue(Integer count, String text) {
        return String.format("%d %s", count, Utils.getLocalized(text));
    }

    private String convert(List<String> strings) {
        StringBuffer buffer = new StringBuffer();
        for (String string : strings) {
            if (buffer.length() > 0) {
                buffer.append(", ");
            }
            buffer.append(Utils.getLocalized(string));
        }
        return buffer.toString();
    }

    private View getDotTitleView(String title, Integer colorRes) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_calendar_list_item, null, false);
        View vCircle = view.findViewById(R.id.v_circle);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        vCircle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, colorRes)));
        tvTitle.setText(Utils.getLocalized(title));
        return view;
    }

    @Override
    public int getItemCount() {
        return mCalendarItems.size();
    }

    static class CalendarListHolder extends RecyclerView.ViewHolder {
        private CalendarListListener mListener;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.ll_container) LinearLayout llContainer;

        public CalendarListHolder(View itemView, CalendarListListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.ll_main)
        void onClick() {
            if (mListener != null) {
                mListener.daySelected(getLayoutPosition());
            }
        }
    }

    public void update(List<CalendarItem> calendarItems, CalendarFilter calendarFilter) {
        if (calendarItems != null) {
            mCalendarItems.clear();
            mCalendarItems.addAll(calendarItems);
            notifyDataSetChanged();
        }
        if (calendarFilter != null) {
            mCalendarFilter = calendarFilter;
        }
    }

    public interface CalendarListListener {
        void daySelected(int position);
    }
}
