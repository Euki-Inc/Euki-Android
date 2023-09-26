package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kollectivemobile.euki.R;

import java.util.Arrays;
import java.util.List;

public class SegmentedButton extends LinearLayout {
    private List<SegmentedItem> items;
    private int selectedIndex;
    private SegmentedButtonListener mListener;

    public SegmentedButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SegmentedButton, 0, 0);
        String titleLeft = array.getString(R.styleable.SegmentedButton_titleLeft).toUpperCase();
        String titleRight = array.getString(R.styleable.SegmentedButton_titleRight).toUpperCase();
        Integer currentIndex = array.getInt(R.styleable.SegmentedButton_selectedIndex, 0);

        array.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_segmented_buttons, this, true);

        items = Arrays.asList(new SegmentedItem[]{findViewById(R.id.si_left), findViewById(R.id.si_right)});

        items.get(0).setTitle(titleLeft);
        items.get(1).setTitle(titleRight);
        selectedIndex = currentIndex;

        setUIElements();
        updateUIElements();
    }

    private void setUIElements() {
        for (int index = 0; index < items.size(); index++) {
            SegmentedItem item = items.get(index);
            final int finalI = index;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCurrentItem(finalI);
                }
            });
        }
    }

    private void updateUIElements() {
        for (int index = 0; index < items.size(); index++) {
            SegmentedItem item = items.get(index);
            Boolean isSelected = index == selectedIndex;
            item.setSelected(isSelected);
        }
    }

    public void setListener(SegmentedButtonListener listener) {
        this.mListener = listener;
    }

    public void setCurrentItem(int position) {
        if (selectedIndex == position) {
            return;
        }

        if (mListener != null) {
            mListener.onSegmentedChanged(position);
        }

        selectedIndex = position;
        updateUIElements();
    }
}
