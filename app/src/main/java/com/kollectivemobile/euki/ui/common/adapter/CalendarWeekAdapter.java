package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.calendar.weeklycalendar.DayItem;
import com.kollectivemobile.euki.ui.common.holder.CalendarDayHolder;
import com.kollectivemobile.euki.ui.common.listeners.CalendarDayListener;
import com.kollectivemobile.euki.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarWeekAdapter extends RecyclerView.Adapter {
    private Map<String, CalendarItem> mCalendarItems;
    private CalendarFilter mCalendarFilter;
    private List<DayItem> mItems;
    private Context mContext;
    private CalendarDayListener mListener;

    private Date mSelectedDate;
    private Date mToday;

    public CalendarWeekAdapter(Context context, Date selectedDate, CalendarDayListener listener) {
        mCalendarItems = new HashMap<>();
        mCalendarFilter = new CalendarFilter();
        mItems = new ArrayList<>();
        mContext = context;
        mSelectedDate = selectedDate;
        mListener = listener;
        mToday = new Date();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_calendar_month_day, parent, false);

        view.setLayoutParams(new RecyclerView.LayoutParams(
                ((RecyclerView) parent).getLayoutManager().getWidth() / 7,
                ((RecyclerView) parent).getLayoutManager().getHeight()));


        return new CalendarDayHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalendarDayHolder) {
            CalendarDayHolder dayHolder = (CalendarDayHolder)holder;
            DayItem dayItem = mItems.get(position);
            final Date date = dayItem.getDate();

            Date selectedDate = mSelectedDate != null ? mSelectedDate : new Date();

            Boolean isSelectedDate = DateUtils.isSameDate(date, selectedDate);
            Boolean isToday = DateUtils.isSameDate(mToday, date);
            Boolean isPrediction = false;

            dayHolder.vClickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DateUtils.isSameDate(mSelectedDate, date)) {
                        return;
                    }

                    if (DateUtils.isFuture(date)) {
                        return;
                    }

                    mSelectedDate = date;
                    notifyDataSetChanged();
                    mListener.daySelected(date);
                }
            });

            dayHolder.ivBlur.setVisibility(isSelectedDate ? View.VISIBLE : View.GONE);

            CalendarItem calendarItem = mCalendarItems.get(DateUtils.toString(date, DateUtils.DateLongFormat));
            dayHolder.setup(date, true, calendarItem, mCalendarFilter, isSelectedDate, isToday, isPrediction);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateObjects(List<DayItem> items, Map<String, CalendarItem> calendarItems, CalendarFilter calendarFilter) {
        mCalendarItems.clear();
        mCalendarItems.putAll(calendarItems);
        mCalendarFilter = calendarFilter;

        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}
