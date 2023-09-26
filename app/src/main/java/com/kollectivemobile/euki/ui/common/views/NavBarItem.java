package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

import com.kollectivemobile.euki.R;

public class NavBarItem extends LinearLayout {
    private View mView;
    private ImageView ivBackground;
    private ImageView ivIcon;
    private TextView tvTitle;

    private Boolean mIsSelected;
    private int mResIdNormal;
    private int mResIdSelected;

    public NavBarItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NavBarItem, 0, 0);
        String title = array.getString(R.styleable.NavBarItem_title);
        Boolean isSelected = array.getBoolean(R.styleable.NavBarItem_selected, false);
        Boolean isCentered = array.getBoolean(R.styleable.NavBarItem_centered, true);

        TypedValue normalValue = new TypedValue();
        array.getValue(R.styleable.NavBarItem_imageResNormal, normalValue);
        mResIdNormal = normalValue.resourceId;

        TypedValue selectedValue = new TypedValue();
        array.getValue(R.styleable.NavBarItem_selected, selectedValue);
        mResIdSelected = normalValue.resourceId;

        array.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_tabbar_item, this, true);
        mView = getChildAt(0);

        ivBackground = mView.findViewById(R.id.iv_background);
        ivIcon = mView.findViewById(R.id.iv_icon);
        tvTitle = mView.findViewById(R.id.tv_title);

        mIsSelected = isSelected;

        tvTitle.setText(title);
        ivIcon.setImageResource(mResIdNormal);

        if (isCentered) {
            RelativeLayout.LayoutParams backgroundParams = (RelativeLayout.LayoutParams) ivBackground.getLayoutParams();
            backgroundParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ivBackground.setLayoutParams(backgroundParams);

            View vContent = findViewById(R.id.ll_content);
            RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) vContent.getLayoutParams();
            contentParams.addRule(RelativeLayout.CENTER_VERTICAL);
            vContent.setLayoutParams(contentParams);

            findViewById(R.id.v_separator).setVisibility(View.GONE);
        } else {
            ivIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        updateUIElements();
    }

    private void updateUIElements() {
        ivBackground.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
        int textStyle = mIsSelected ? R.style.NavTextSelected : R.style.NavTextNormal;
        TextViewCompat.setTextAppearance(tvTitle, textStyle);

        int iconRes = mIsSelected ? mResIdSelected : mResIdNormal;
        ivIcon.setImageResource(iconRes);
    }

    public void setSelected(Boolean selected) {
        this.mIsSelected = selected;
        updateUIElements();
    }
}
