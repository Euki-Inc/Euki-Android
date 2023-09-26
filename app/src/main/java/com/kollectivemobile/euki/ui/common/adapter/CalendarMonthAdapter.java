package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CalendarFilter;
import com.kollectivemobile.euki.model.MonthItem;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.holder.CalendarDayHolder;
import com.kollectivemobile.euki.ui.common.listeners.CalendarDayListener;
import com.kollectivemobile.euki.utils.DateUtils;

import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarMonthAdapter extends RecyclerView.Adapter {
    private final Integer VIEW_TYPE_HEADER = 0;
    private final Integer VIEW_TYPE_EMPTY = 1;
    private final Integer VIEW_TYPE_DAY = 2;

    private Map<String, CalendarItem> mCalendarItems;
    private CalendarFilter mCalendarFilter;

    private List<Object> mObjects;
    private Context mContext;
    private CalendarDayListener mListener;
    private Date mToday;
    private List<Range<Date>> mPredictionRange;

    public CalendarMonthAdapter(Context context, CalendarDayListener listener) {
        mCalendarItems = new HashMap<>();
        mCalendarFilter = new CalendarFilter();
        mObjects = new ArrayList<>();
        mContext = context;
        mListener = listener;
        mToday = new Date();
        mPredictionRange = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.layout_calendar_month_header, parent, false);
            return new CalendarHeaderHolder(view);
        }
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = new View(mContext);
            return new CalendarEmptyHolder(view);
        }

        View view = inflater.inflate(R.layout.layout_calendar_month_day, parent, false);
        return new CalendarDayHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalendarHeaderHolder && mObjects.get(position) instanceof String) {
            CalendarHeaderHolder headerHolder = (CalendarHeaderHolder)holder;
            String title = (String)mObjects.get(position);
            headerHolder.tvTitle.setText(title);
            return;
        }

        if (holder instanceof CalendarDayHolder && mObjects.get(position) instanceof Date) {
            CalendarDayHolder dayHolder = (CalendarDayHolder)holder;
            Date date = (Date)mObjects.get(position);
            Boolean isToday = DateUtils.isSameDate(mToday, date);

            Boolean isPrediction = false;
            for (Range<Date> range : mPredictionRange) {
                if (range.getMinimum().getTime() <= date.getTime() && date.getTime() <= range.getMaximum().getTime()) {
                    isPrediction = true;
                    break;
                }
            }

            CalendarItem calendarItem = mCalendarItems.get(DateUtils.toString(date, DateUtils.DateLongFormat));
            dayHolder.setup(date, false, calendarItem, mCalendarFilter, isToday, false, isPrediction);
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mObjects.get(position);
        if (object instanceof String) {
            String value = (String)object;
            if (value.isEmpty()) {
                return VIEW_TYPE_EMPTY;
            }
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_DAY;
    }

    public void updateObjects(List<MonthItem> monthItems, Map<String, CalendarItem> calendarItems, CalendarFilter calendarFilter, List<Range<Date>> predictionRange) {
        mPredictionRange = predictionRange;
        mCalendarItems.clear();
        mCalendarItems.putAll(calendarItems);
        mCalendarFilter = calendarFilter;

        List<Object> objects = new ArrayList<>();
        for (MonthItem monthItem : monthItems) {
            monthItem.setMinIndex(objects.size());

            objects.add(monthItem.getTitle());

            Integer firstDayWeek = monthItem.getFirstDayWeek();

            if (firstDayWeek > 1) {
                for (int i=1; i<firstDayWeek; i++) {
                    objects.add("");
                }
            }

            for (int i=1; i<=monthItem.getNumDays(); i++) {
                Date date = monthItem.getDate(i);
                objects.add(date);
            }

            Integer restDays = (objects.size() - 1) % 7;

            if (restDays > 0) {
                for (int i=0; i<restDays; i++) {
                    objects.add("");
                }
            }
        }

        mObjects.clear();
        mObjects.addAll(objects);
        notifyDataSetChanged();
    }

    public List<Object> getObjects() {
        return mObjects;
    }

    public class CalendarHeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title) TextView tvTitle;

        public CalendarHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CalendarEmptyHolder extends RecyclerView.ViewHolder {
        public CalendarEmptyHolder(View itemView) {
            super(itemView);
        }
    }
}
