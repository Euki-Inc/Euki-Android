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

public class DailyLogBodyHolder extends BaseDailyLogHolder implements View.OnClickListener {
    public @BindViews({R.id.sb_body1, R.id.sb_body2, R.id.sb_body3, R.id.sb_body4,
                       R.id.sb_body5, R.id.sb_body6, R.id.sb_body7, R.id.sb_body8,
                       R.id.sb_body9, R.id.sb_body10, R.id.sb_body11, R.id.sb_body12,
                       R.id.sb_body13, R.id.sb_body14}) List<SelectableButton> sbBody;

    public DailyLogBodyHolder(@NonNull View itemView, DailyLogViewListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);

        for (SelectableButton selectableButton : sbBody) {
            selectableButton.setOnClickListener(this);
        }
    }

    static public DailyLogBodyHolder create(ViewGroup parent, DailyLogViewListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_body, parent, false);
        return new DailyLogBodyHolder(view, listener);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.BODY;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasBody();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);

        for (Constants.Body body : mCalendarItem.getBody()) {
            sbBody.get(body.ordinal()).changeSelected(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SelectableButton && view.getTag() instanceof String) {
            Integer index = Integer.parseInt((String)view.getTag());
            Constants.Body body = Constants.Body.values()[index - 1];

            if (mCalendarItem.getBody().contains(body)) {
                mCalendarItem.getBody().remove(body);
            } else {
                mCalendarItem.getBody().add(body);
            }

            updateTitle();
            mListener.dataChanged();
        }
    }
}
