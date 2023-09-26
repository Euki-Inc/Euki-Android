package com.kollectivemobile.euki.ui.common.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.listeners.DailyLogViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseDailyLogHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ll_title) LinearLayout llTitle;
    @BindView(R.id.v_circle) View vCircle;
    @BindView(R.id.ll_content) LinearLayout llContent;

    protected CalendarItem mCalendarItem;
    protected DailyLogViewListener mListener;
    protected Boolean isSelected = false;

    public BaseDailyLogHolder(@NonNull View itemView, DailyLogViewListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
    }

    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        itemView.setAlpha(selectedType == null || selectedType == getViewType() ? 1.0f : 0.4f);
        mCalendarItem = calendarItem;
        isSelected = selected;
        llContent.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        updateTitle();
    }

    public void updateTitle() {
        vCircle.setBackgroundResource(hasData() ? R.drawable.bkg_circular_daily_log_on : R.drawable.bkg_circular_daily_log_off);
    }

    abstract public DailyLogAdapter.ViewType getViewType();

    abstract public Boolean hasData();

    @OnClick(R.id.ll_title)
    void titleClicked() {
        if (mListener != null) {
            mListener.selectedViewType(getViewType(), getLayoutPosition());
        }
    }
}
