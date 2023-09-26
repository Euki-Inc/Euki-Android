package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;

public class SegmentedItem extends LinearLayout {
    private View mView;
    private ImageView ivBackground;
    private TextView tvTitle;
    private View vBorder;

    private Boolean mIsSelected;

    public SegmentedItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SegmentedItem, 0, 0);
        String title = array.getString(R.styleable.SegmentedItem_title);
        Boolean isSelected = array.getBoolean(R.styleable.SegmentedItem_selected, false);

        array.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_segmented_item, this, true);
        mView = getChildAt(0);

        ivBackground = mView.findViewById(R.id.iv_background);
        tvTitle = mView.findViewById(R.id.tv_title);
        vBorder = mView.findViewById(R.id.v_border);

        mIsSelected = isSelected;

        tvTitle.setText(title);

        updateUIElements();
    }

    private void updateUIElements() {
        ivBackground.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
        int color = mIsSelected ? R.color.iris : R.color.primary_lighter;
        vBorder.setBackgroundColor(ContextCompat.getColor(App.getContext(), color));
    }

    public void setSelected(Boolean selected) {
        this.mIsSelected = selected;
        updateUIElements();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
