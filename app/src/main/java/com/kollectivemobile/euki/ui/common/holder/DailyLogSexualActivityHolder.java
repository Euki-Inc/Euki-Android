package com.kollectivemobile.euki.ui.common.holder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.listeners.DailyLogViewListener;
import com.kollectivemobile.euki.ui.common.views.SelectableButton;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class DailyLogSexualActivityHolder extends BaseDailyLogHolder implements View.OnClickListener {
    public @BindViews({R.id.sb_sexual_activity_sti_1, R.id.sb_sexual_activity_sti_2}) List<SelectableButton> sbProtectionSTI;
    public @BindViews({R.id.sb_sexual_activity_pregnancy_1, R.id.sb_sexual_activity_pregnancy_2}) List<SelectableButton> sbProtectionPregnancy;
    public @BindViews({R.id.sb_sexual_activity_other_1, R.id.sb_sexual_activity_other_2, R.id.sb_sexual_activity_other_3,
                       R.id.sb_sexual_activity_other_4, R.id.sb_sexual_activity_other_5}) List<SelectableButton> sbProtectionOther;

    public DailyLogSexualActivityHolder(@NonNull View itemView, DailyLogViewListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);

        for (SelectableButton selectableButton : sbProtectionSTI) {
            selectableButton.setOnClickListener(this);
        }
        for (SelectableButton selectableButton : sbProtectionPregnancy) {
            selectableButton.setOnClickListener(this);
        }
        for (SelectableButton selectableButton : sbProtectionOther) {
            selectableButton.setOnClickListener(this);
        }
    }

    static public DailyLogSexualActivityHolder create(ViewGroup parent, DailyLogViewListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_sexual_activity, parent, false);
        return new DailyLogSexualActivityHolder(view, listener);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.SEXUALACTIVITY;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasSexualActivity();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);

        for (int i=0; i<sbProtectionSTI.size(); i++) {
            SelectableButton selectableButton = sbProtectionSTI.get(i);
            selectableButton.setCounter(mCalendarItem.getSexualProtectionSTICounter().get(i));
        }
        for (int i=0; i<sbProtectionPregnancy.size(); i++) {
            SelectableButton selectableButton = sbProtectionPregnancy.get(i);
            selectableButton.setCounter(mCalendarItem.getSexualProtectionPregnancyCounter().get(i));
        }
        for (int i=0; i<sbProtectionOther.size(); i++) {
            SelectableButton selectableButton = sbProtectionOther.get(i);
            selectableButton.setCounter(mCalendarItem.getSexualOtherCounter().get(i));
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SelectableButton && view.getTag() instanceof String) {
            SelectableButton selectableButton = (SelectableButton)view;
            Integer index = Integer.parseInt((String)view.getTag());

            if (sbProtectionSTI.contains(selectableButton)) {
                mCalendarItem.getSexualProtectionSTICounter().set(index - 1, selectableButton.getCounter());
            } else if (sbProtectionPregnancy.contains(selectableButton)) {
                mCalendarItem.getSexualProtectionPregnancyCounter().set(index - 1, selectableButton.getCounter());
            } else if (sbProtectionOther.contains(selectableButton)) {
                mCalendarItem.getSexualOtherCounter().set(index - 1, selectableButton.getCounter());
            }

            updateTitle();
            mListener.dataChanged();
        }
    }
}
