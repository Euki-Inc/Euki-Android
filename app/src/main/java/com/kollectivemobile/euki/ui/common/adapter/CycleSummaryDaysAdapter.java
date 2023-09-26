package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.strings.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CycleSummaryDaysAdapter extends RecyclerView.Adapter {

    public static final Integer VIEW_TYPE_EMPTY = 0;
    public static final Integer VIEW_TYPE_ITEM = 1;
    private List<CycleDayItem> mItems = new ArrayList<>();
    private WeakReference<Context> mContext;

    public CycleSummaryDaysAdapter(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_EMPTY) {
            View view = inflater.inflate(R.layout.layout_cycle_summary_day_empty, parent, false);
            return new CycleSummaryEmptyHolder(view);
        }
        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.layout_cycle_summary_day_item, parent, false);
            return new CycleSummaryDaysHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CycleSummaryEmptyHolder) {
            return;
        }

        CycleSummaryDaysHolder rowHolder = (CycleSummaryDaysHolder) holder;
        CycleDayItem item = mItems.get(position - 1);

        String dateString = StringUtils.capitalizeAll(DateUtils.toString(item.getDate(), DateUtils.eeeMMMdd));
        rowHolder.tvDate.setText(dateString);

        Integer dayCycle = item.getDayCycle();
        if (dayCycle != null) {
            String cycleDay = String.format(App.getContext().getString(R.string.cycle_day_format), dayCycle);
            rowHolder.tvCycleDay.setText(cycleDay);
            rowHolder.tvCycleDay.setVisibility(View.VISIBLE);
        } else {
            rowHolder.tvCycleDay.setVisibility(View.GONE);
        }

        Date dateNextCycle = item.getDateNextCycle();
        if (dateNextCycle != null) {
            String nextCycleString = DateUtils.toString(dateNextCycle, DateUtils.MMMdd);
            if (nextCycleString != null) {
                nextCycleString = StringUtils.capitalize(nextCycleString);
            }
            rowHolder.tvNextCycleDay.setText(nextCycleString);
            rowHolder.llNextCycle.setVisibility(View.VISIBLE);
        } else {
            rowHolder.llNextCycle.setVisibility(View.GONE);
        }

        if (item.getCalendarItem() != null && item.getCalendarItem().hasPeriod()) {
            rowHolder.llContainer.setBackgroundResource(R.drawable.bkg_circular_day_summary_bleeding);
        } else {
            rowHolder.llContainer.setBackgroundResource(R.drawable.bkg_circular_day_summary_normal);
        }
    }

    @Override
    public int getItemCount() {
        if (mItems.size() == 0) {
            return 0;
        }
        return mItems.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mItems.size() + 1) {
            return VIEW_TYPE_EMPTY;
        }
        return VIEW_TYPE_ITEM;
    }

    public class CycleSummaryEmptyHolder extends RecyclerView.ViewHolder {
        public CycleSummaryEmptyHolder(View itemView) {
            super(itemView);
        }
    }

    public class CycleSummaryDaysHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_container) View llContainer;
        @BindView(R.id.ll_content) View llContent;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.tv_cycle_day) TextView tvCycleDay;
        @BindView(R.id.ll_next_cycle) View llNextCycle;
        @BindView(R.id.tv_next_cycle_day) TextView tvNextCycleDay;

        public CycleSummaryDaysHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void update(List<CycleDayItem> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }
}
