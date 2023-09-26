package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kollectivemobile.euki.R;

import java.util.Arrays;
import java.util.List;

public class NavBar extends LinearLayout {
    private List<NavBarItem> items;
    private int selectedIndex;
    private NavBarListener mListener;

    public NavBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_tabbar, this, true);

        items = Arrays.asList(new NavBarItem[] { findViewById(R.id.nb_1), findViewById(R.id.nb_2), findViewById(R.id.nb_3), findViewById(R.id.nb_4), findViewById(R.id.nb_5) });

        setUIElements();
        updateUIElements();
    }

    private void setUIElements() {
        for (int index = 0; index < items.size(); index++) {
            NavBarItem item = items.get(index);
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
            NavBarItem item = items.get(index);
            Boolean isSelected = index == selectedIndex;
            item.setSelected(isSelected);
        }
    }

    public void setListener(NavBarListener listener) {
        this.mListener = listener;
    }

    public void setCurrentItem(int position) {
        Boolean shouldSelect = false;
        if (mListener != null) {
            shouldSelect = mListener.onTabSelected(position);
        }

        if (shouldSelect) {
            selectedIndex = position;
            updateUIElements();
        }

        updateUIElements();
    }

    public View getViewAtPosition(int position) {
        if (position == 2) {
            return findViewById(R.id.v_tutorial_3);
        }
        return items.get(position);
    }
}
