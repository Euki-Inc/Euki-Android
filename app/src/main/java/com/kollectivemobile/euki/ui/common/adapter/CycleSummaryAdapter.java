package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CyclePeriodData;
import com.kollectivemobile.euki.model.CyclePeriodItem;
import com.kollectivemobile.euki.utils.DateUtils;
import com.kollectivemobile.euki.utils.strings.StringUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CycleSummaryAdapter extends RecyclerView.Adapter {
    public static final Integer VIEW_TYPE_TODAY = 0;
    public static final Integer VIEW_TYPE_ITEM = 1;
    public static final Integer VIEW_TYPE_SEE_PAST = 2;

    private Context mContext;

    private CyclePeriodData mCyclePeriodData;
    private Boolean showAll = false;

    public CycleSummaryAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SEE_PAST) {
            View view = inflater.inflate(R.layout.layout_cycle_summary_see_past, parent, false);
            return new SeePastHolder(view);
        }
        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.layout_cycle_summary_item, parent, false);
            return new ItemHolder(view);
        }
        if (viewType == VIEW_TYPE_TODAY) {
            View view = inflater.inflate(R.layout.layout_cycle_summary_item, parent, false);
            return new TodayHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SeePastHolder) {
            SeePastHolder rowHolder = (SeePastHolder) holder;
            rowHolder.tvSeePast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAll = true;
                    notifyDataSetChanged();
                }
            });
            return;
        }

        Date now = new Date();
        ItemHolder rowHolder = (ItemHolder) holder;

        Integer maxWidth  = Resources.getSystem().getDisplayMetrics().widthPixels - (int)mContext.getResources().getDimension(R.dimen.dimen_x6_5);

        if (mCyclePeriodData.getItems().isEmpty()) {
            rowHolder.tvRange.setText(mContext.getString(R.string.date_starting_period));
            rowHolder.tvBleeding.setText("-");

            RelativeLayout.LayoutParams periodParams = (RelativeLayout.LayoutParams) rowHolder.rlSliders.getLayoutParams();
            periodParams.setMargins(0, 0, 0, 0);
            rowHolder.rlSliders.setLayoutParams(periodParams);

            RelativeLayout.LayoutParams bleedingParams = (RelativeLayout.LayoutParams) rowHolder.rlBleeding.getLayoutParams();
            bleedingParams.setMargins(0, 0, maxWidth * 5 / 6, 0);
            rowHolder.rlBleeding.setLayoutParams(bleedingParams);

            rowHolder.vDayIndicator.setVisibility(View.GONE);
            return;
        }

        CyclePeriodItem item = mCyclePeriodData.getItems().get(position);
        Boolean isCurrentPeriod = DateUtils.isSameDate(item.getEndDate(), now);

        String startString = StringUtils.capitalize(DateUtils.toString(item.getInitialDate(), DateUtils.MMMdd));
        String endString;
        String daysString;

        if (DateUtils.isSameDate(item.getEndDate(), now)) {
            endString = mContext.getString(R.string.cycle_today);
            daysString = "";
        } else {
            endString = StringUtils.capitalize(DateUtils.toString(item.getEndDate(), DateUtils.MMMdd));
            daysString = (DateUtils.daysDiff(item.getInitialDate(), item.getEndDate()) + 1) + " " + mContext.getString(R.string.days).toLowerCase() + ", ";
        }

        rowHolder.tvRange.setText(daysString + " " + startString + " - " + endString);

        Integer maxCycleLength = mCyclePeriodData.getMaxCycleLength();
        if (maxCycleLength == null) {
            maxCycleLength = 30;
        }

        long cycleLength = DateUtils.daysDiff(item.getInitialDate(), item.getEndDate());

        int cycleMargin = (int)(maxWidth * (1.0 - cycleLength / (double)maxCycleLength));
        int periodMargin = (int)(maxWidth * (1.0 - item.getDuration() / (double)maxCycleLength)) - (int)mContext.getResources().getDimension(R.dimen.dimen_x5_5);

        if (isCurrentPeriod) {
            cycleMargin = 0;
        }

        RelativeLayout.LayoutParams periodParams = (RelativeLayout.LayoutParams) rowHolder.rlSliders.getLayoutParams();
        periodParams.setMargins(0, 0, cycleMargin, 0);
        rowHolder.rlSliders.setLayoutParams(periodParams);

        RelativeLayout.LayoutParams bleedingParams = (RelativeLayout.LayoutParams) rowHolder.rlBleeding.getLayoutParams();
        bleedingParams.setMargins(0, 0, periodMargin, 0);
        rowHolder.rlBleeding.setLayoutParams(bleedingParams);

        rowHolder.tvBleeding.setText(item.getDuration() + "");

        Integer currentDayCycle = mCyclePeriodData.getCurrentDayCycle();
        if (currentDayCycle != null && isCurrentPeriod) {
            int dayMargin = (int)(maxWidth * currentDayCycle / maxCycleLength + mContext.getResources().getDimension(R.dimen.dimen_x2_9));
            rowHolder.vDayIndicator.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) rowHolder.vDayIndicator.getLayoutParams();
            indicatorParams.setMargins(dayMargin, 0, 0, 0);
            rowHolder.vDayIndicator.setLayoutParams(indicatorParams);
        } else {
            rowHolder.vDayIndicator.setVisibility(View.GONE);
        }

        if (position > 0) {
            //TODO: Add Remove button
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCyclePeriodData == null || mCyclePeriodData.getItems().isEmpty() || showAll) {
            if (mCyclePeriodData.getItems().size() >= 0 && position == 0) {
                return VIEW_TYPE_TODAY;
            }
            return VIEW_TYPE_ITEM;
        }

        if (position == 0) {
            return VIEW_TYPE_TODAY;
        }

        return VIEW_TYPE_SEE_PAST;
    }

    @Override
    public int getItemCount() {
        if (mCyclePeriodData == null) {
            return 0;
        }

        if (mCyclePeriodData.getItems().size() < 2) {
            return 1;
        }

        if (showAll) {
            return mCyclePeriodData.getItems().size();
        }

        return 2;
    }

    public class TodayHolder extends ItemHolder {
        public TodayHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_range) TextView tvRange;
        @BindView(R.id.tv_bleeding) TextView tvBleeding;
        @BindView(R.id.v_day_indicator) View vDayIndicator;
        @BindView(R.id.rl_sliders) RelativeLayout rlSliders;
        @BindView(R.id.rl_bleeding) RelativeLayout rlBleeding;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class SeePastHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_see_past) TextView tvSeePast;

        public SeePastHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void update(CyclePeriodData data) {
        mCyclePeriodData = data;
        notifyDataSetChanged();
    }
}
