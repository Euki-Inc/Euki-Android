package com.kollectivemobile.euki.ui.common.holder;

import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.database.entity.CalendarItem;
import com.kollectivemobile.euki.ui.common.adapter.DailyLogAdapter;
import com.kollectivemobile.euki.ui.common.listeners.DailyLogViewListener;


public class DailyLogNoteHolder extends BaseDailyLogHolder {
    private EditText etTitle;
    private TextView tvCounter;

    public DailyLogNoteHolder(@NonNull View itemView, DailyLogViewListener listener) {
        super(itemView, listener);

        // Manual view binding
        etTitle = itemView.findViewById(R.id.et_title);
        tvCounter = itemView.findViewById(R.id.tv_counter);

        // Set up text watcher for the EditText
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() > 200) {
                    text = text.substring(0, 200);
                    etTitle.setText(text);
                    etTitle.setSelection(text.length());
                }
                mCalendarItem.setNote(text);
                updateCounter();
                updateTitle();
                mListener.dataChanged();
            }
        });
    }

    public static DailyLogNoteHolder create(ViewGroup parent, DailyLogViewListener listener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_note, parent, false);
        return new DailyLogNoteHolder(view, listener);
    }

    @Override
    public DailyLogAdapter.ViewType getViewType() {
        return DailyLogAdapter.ViewType.NOTE;
    }

    @Override
    public Boolean hasData() {
        return mCalendarItem.hasNote();
    }

    @Override
    public void bind(CalendarItem calendarItem, Boolean selected, DailyLogAdapter.ViewType selectedType) {
        super.bind(calendarItem, selected, selectedType);
        etTitle.setText(calendarItem.getNote());

        if (calendarItem.getNote() != null && !calendarItem.getNote().isEmpty()) {
            etTitle.setSelection(calendarItem.getNote().length());
        }

        updateCounter();
        updateTitle();
    }

    private void updateCounter() {
        int length = etTitle.getText().toString().length();
        tvCounter.setText(length + "/200");
    }
}
