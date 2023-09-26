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
import com.kollectivemobile.euki.utils.Constants;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class DailyLogTestHolder extends BaseDailyLogHolder implements View.OnClickListener {
    public @BindViews({R.id.sb_test_sti_1, R.id.sb_test_sti_2}) List<SelectableButton> sbTestSTI;
    public @BindViews({R.id.sb_test_pregnancy_1, R.id.sb_test_pregnancy_2}) List<SelectableButton> sbTestPregnancy;

    public DailyLogTestHolder(@NonNull View itemView, DailyLogViewListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);

        for (SelectableButton selectableButton : sbTestSTI) {
            selectableButton.setOnClickListener(this);
        }
        for (SelectableButton selectableButton : sbTestPregnancy) {
            selectableButton.setOnClickListener(this);
        }
    }

    static public DailyLogTestHolder create(ViewGroup parent, DailyLogViewListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_test, parent, false);
        return new DailyLogTestHolder(view, listener);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.TEST;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasTest();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);

        if (mCalendarItem.getTestSTI() != null) {
            sbTestSTI.get(mCalendarItem.getTestSTI().ordinal()).changeSelected(true);
        }
        if (mCalendarItem.getTestPregnancy() != null) {
            sbTestPregnancy.get(mCalendarItem.getTestPregnancy().ordinal()).changeSelected(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SelectableButton && view.getTag() instanceof String) {
            SelectableButton selectableButton = (SelectableButton)view;
            Integer index = Integer.parseInt((String)view.getTag());

            if (sbTestSTI.contains(selectableButton)) {
                mCalendarItem.setTestSTI(selectableButton.getSelected() ? Constants.TestSTI.values()[index-1] : null);
            } else if (sbTestPregnancy.contains(selectableButton)) {
                mCalendarItem.setTestPregnancy(selectableButton.getSelected() ? Constants.TestPregnancy.values()[index-1] : null);
            }

            updateTitle();
            mListener.dataChanged();
        }
    }
}
