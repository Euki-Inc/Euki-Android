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

public class DailyLogEmotionsHolder extends BaseDailyLogHolder implements View.OnClickListener {
    public @BindViews({R.id.sb_emotions_1, R.id.sb_emotions_2, R.id.sb_emotions_3,
                       R.id.sb_emotions_4, R.id.sb_emotions_5, R.id.sb_emotions_6,
                       R.id.sb_emotions_7, R.id.sb_emotions_8, R.id.sb_emotions_9}) List<SelectableButton> sbEmotions;

    public DailyLogEmotionsHolder(@NonNull View itemView, DailyLogViewListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);

        for (SelectableButton selectableButton : sbEmotions) {
            selectableButton.setOnClickListener(this);
        }
    }

    static public DailyLogEmotionsHolder create(ViewGroup parent, DailyLogViewListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_emotions, parent, false);
        return new DailyLogEmotionsHolder(view, listener);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.EMOTIONS;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasEmotions();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);

        for (Constants.Emotions emotions : mCalendarItem.getEmotions()) {
            sbEmotions.get(emotions.ordinal()).changeSelected(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof SelectableButton && view.getTag() instanceof String) {
            Integer index = Integer.parseInt((String)view.getTag());
            Constants.Emotions emotion = Constants.Emotions.values()[index - 1];

            if (mCalendarItem.getEmotions().contains(emotion)) {
                mCalendarItem.getEmotions().remove(emotion);
            } else {
                mCalendarItem.getEmotions().add(emotion);
            }

            updateTitle();
            mListener.dataChanged();
        }
    }
}
