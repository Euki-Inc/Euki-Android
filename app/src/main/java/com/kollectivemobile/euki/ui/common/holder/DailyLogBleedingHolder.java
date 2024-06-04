package com.kollectivemobile.euki.ui.common.holder;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.listeners.DailyLogViewListener;
import com.kollectivemobile.euki.ui.common.views.SelectableButton;
import com.kollectivemobile.euki.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DailyLogBleedingHolder extends BaseDailyLogHolder implements View.OnClickListener {
    public @BindViews({R.id.sb_bleeding_size_1, R.id.sb_bleeding_size_2, R.id.sb_bleeding_size_3,
            R.id.sb_bleeding_size_4}) List<SelectableButton> sbBleedingSizes;
    public @BindViews({R.id.sb_bleeding_product_1, R.id.sb_bleeding_product_2,
            R.id.sb_bleeding_product_3, R.id.sb_bleeding_product_4,
            R.id.sb_bleeding_product_5, R.id.sb_bleeding_product_6, R.id.sb_bleeding_product_7}) List<SelectableButton> sbBleedingProducts;

    public @BindViews({R.id.sb_clot_1, R.id.sb_clot_2}) List<SelectableButton> sbBleedingClots;

    @BindView(R.id.tv_info)
    ImageView ivInfo;
    @BindView(R.id.ll_include_cycle)
    LinearLayout llIncludeCycle;
    @BindView(R.id.iv_include_cycle)
    ImageView ivIncludeCycle;

    private boolean mBleedingTrackingEnabled;

    public DailyLogBleedingHolder(@NonNull View itemView, DailyLogViewListener listener, Boolean bleedingTrackingEnabled) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);

        mBleedingTrackingEnabled = bleedingTrackingEnabled;

        for (SelectableButton selectableButton : sbBleedingSizes) {
            selectableButton.setOnClickListener(this);
        }
        for (SelectableButton selectableButton : sbBleedingProducts) {
            selectableButton.setOnClickListener(this);
        }
        for (SelectableButton selectableButton : sbBleedingClots) {
            selectableButton.setOnClickListener(this);
        }
    }

    static public DailyLogBleedingHolder create(ViewGroup parent, DailyLogViewListener listener, Boolean bleedingTrackingEnabled) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_bleeding, parent, false);
        return new DailyLogBleedingHolder(view, listener, bleedingTrackingEnabled);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.BLEEDING;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasBleeding();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);

        if (calendarItem.getBleedingSize() != null) {
            sbBleedingSizes.get(calendarItem.getBleedingSize().ordinal()).changeSelected(true);
        }
        for (int i = 0; i < sbBleedingProducts.size(); i++) {
            SelectableButton selectableButton = sbBleedingProducts.get(i);
            selectableButton.setCounter(mCalendarItem.getBleedingProductsCounter().get(i));
        }
        for (int i = 0; i < sbBleedingClots.size(); i++) {
            SelectableButton selectableButton = sbBleedingClots.get(i);
            selectableButton.setCounter(mCalendarItem.getBleedingClotsCounter().get(i));
        }

        ivInfo.setVisibility(selected ? View.VISIBLE : View.GONE);
        ivInfo.setOnClickListener(view -> mListener.infoAction());

        updateIncludeCycle();
        ivIncludeCycle.setOnClickListener(view -> {
            if (mBleedingTrackingEnabled) {
                mCalendarItem.setIncludeCycleSummary(!mCalendarItem.getIncludeCycleSummary());
                updateIncludeCycle();
            }
        });
        llIncludeCycle.setAlpha(mBleedingTrackingEnabled ? 1.0f : 0.5f);
    }

    private void updateIncludeCycle() {
        if (!mCalendarItem.hasBleeding()) {
            mCalendarItem.setIncludeCycleSummary(false);
        }

        llIncludeCycle.setVisibility(mCalendarItem.hasBleeding() ? View.VISIBLE : View.GONE);
        Integer resId = mCalendarItem.getIncludeCycleSummary() ? R.drawable.ic_track_cycle_on : R.drawable.ic_track_cycle_off;
        ivIncludeCycle.setImageResource(resId);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SelectableButton && view.getTag() instanceof String) {
            SelectableButton selectableButton = (SelectableButton) view;
            Integer index = Integer.parseInt((String) view.getTag());

            if (sbBleedingSizes.contains(selectableButton)) {
                mCalendarItem.setBleedingSize(selectableButton.getSelected() ? Constants.BleedingSize.values()[index - 1] : null);
            } else if (sbBleedingProducts.contains(selectableButton)) {
                mCalendarItem.getBleedingProductsCounter().set(index - 1, selectableButton.getCounter());
            } else if (sbBleedingClots.contains(selectableButton)) {
                mCalendarItem.getBleedingClotsCounter().set(index - 1, selectableButton.getCounter());
            }

            updateTitle();
            updateIncludeCycle();
            mListener.dataChanged();

            if (mCalendarItem.hasBleeding()) {
                mListener.checkIncludeBleedingCycle();
            }
        }
    }
}
