package com.kollectivemobile.euki.ui.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class SelectableButton extends LinearLayout {
    private View mView;
    private View mBorderView;
    private TextView tvCounter;

    private Boolean mSingleSelection = true;
    private Boolean mSelected = false;
    private Integer mCounter = 0;
    private Boolean mIsRadio = false;
    private Boolean mIsEnabled = true;
    private OnClickListener mOnClickListener;

    public SelectableButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectableButton, 0, 0);
        String title = array.getString(R.styleable.SelectableButton_title);
        mIsRadio = array.getBoolean(R.styleable.SelectableButton_isRadio, false);
        mSingleSelection = array.getBoolean(R.styleable.SelectableButton_singleSelection, true);

        TypedValue value = new TypedValue();
        array.getValue(R.styleable.SelectableButton_imageRes, value);
        Integer imageResId = value.resourceId;

        array.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_selectable_button, this, true);
        mView = getChildAt(0);

        ImageView ivIcon = mView.findViewById(R.id.iv_icon);
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        mBorderView = mView.findViewById(R.id.v_border);
        tvCounter = mView.findViewById(R.id.tv_counter);

        tvTitle.setText(Utils.getLocalized(title));
        ivIcon.setImageResource(imageResId);

        updateUIElements();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public SelectableButton(Context context) {
        super(context, null);
    }

    public void changeSelected(Boolean selected) {
        mSingleSelection = true;
        mSelected = selected;
        updateUIElements();
    }

    public Boolean getSelected() {
        return mSelected;
    }

    public Integer getCounter() {
        return mCounter;
    }

    public void setCounter(int counter) {
        mSingleSelection = false;
        mSelected = true;
        mCounter = counter;
        updateUIElements();
    }

    public Boolean getIsEnabled() {
        return mIsEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        mIsEnabled = enabled;
    }

    public void setTitle(String title) {
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        tvTitle.setText(Utils.getLocalized(title));
    }

    public void setImageRes(int imageResId) {
        ImageView ivIcon = mView.findViewById(R.id.iv_icon);
        ivIcon.setImageResource(imageResId);
    }

    private void updateUIElements() {
        Boolean isSelected = false;
        if (mSingleSelection) {
            isSelected = mSelected;
        } else {
            isSelected = mCounter > 0;
            tvCounter.setText(mCounter + "");
        }
        mBorderView.setBackgroundResource(isSelected ? R.drawable.selector_circular_purple : R.drawable.selector_circular_transparent);
        tvCounter.setVisibility(!mSingleSelection && mCounter > 0 ? VISIBLE : GONE);
    }

    private void changeRadioButtons() {
        if (mIsRadio && getParent() == null) {
            return;
        }

        ViewGroup parentView = (ViewGroup)getParent();
        for (int i=0; i<parentView.getChildCount(); i++) {
            View view = parentView.getChildAt(i);

            if (view instanceof SelectableButton) {
                SelectableButton selectableButton = (SelectableButton)view;
                if (selectableButton != this && selectableButton.mIsRadio) {
                    selectableButton.changeSelected(false);
                }
            }
        }
    }

    public void hideBorder() {
        mBorderView.setVisibility(View.GONE);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @OnClick(R.id.v_border)
    void onClick() {
        if (!mIsEnabled) {
            return;
        }

        if (mSingleSelection) {
            changeSelected(!mSelected);
            changeRadioButtons();
        } else {
            mCounter++;
            if (mCounter == 11) {
                mCounter = 0;
            }
            setCounter(mCounter);
        }

        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }

    @OnLongClick(R.id.v_border)
    boolean onLongClick() {
        if (!mIsEnabled) {
            return false;
        }

        if (!mSingleSelection) {
            setCounter(0);
            if (mOnClickListener != null) {
                mOnClickListener.onClick(this);
            }
        }
        return true;
    }
}
