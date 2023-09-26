package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SquaredLinearLayout extends LinearLayout {
    public SquaredLinearLayout(Context context) {
        super(context);
    }

    public SquaredLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquaredLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
