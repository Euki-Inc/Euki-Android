package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.R;

public class MenuItem extends LinearLayout {
    private View mView;
    private ImageView ivBackground;
    private TextView tvTitle;
    private String mTitle;

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MenuItem, 0, 0);
        String title = array.getString(R.styleable.MenuItem_title);
        Integer count = array.getInt(R.styleable.MenuItem_count, 0);
        mTitle = title;

        array.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_menu_item, this, true);
        mView = getChildAt(0);

        ivBackground = mView.findViewById(R.id.iv_background);
        tvTitle = mView.findViewById(R.id.tv_title);

        updateCount(count);
        setTitle(title);
    }

    public void updateCount(Integer count) {
        Boolean isSelected = count > 0;
        String title = mTitle;

        if (count > 0) {
            title = title + "(" + count + ")";
        }

        ivBackground.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        tvTitle.setText(title);
    }

    public void setTitle(String title) {
        if (title == null) {
            return;
        }

        mTitle = title.toUpperCase();
        tvTitle.setText(mTitle);
    }
}
