package com.kollectivemobile.euki.ui.cycle.days;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.ui.common.adapter.CycleSummaryDaysAdapter;

import java.util.ArrayList;
import java.util.List;

public class HorizontalCarouselRecyclerView extends RecyclerView {
    private LinearLayoutManager layoutManager;
    private CycleSummaryDaysAdapter adapter;

    private List<CycleDayItem> mItems = new ArrayList<>();
    private DaysFragmentListener mListener;
    private Integer mCurrentIndex;

    public HorizontalCarouselRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public HorizontalCarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalCarouselRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        layoutManager = new CenterZoomLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(layoutManager);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                colorView();
            }
        });

        adapter = new CycleSummaryDaysAdapter(context);
        setAdapter(adapter);
    }

    public void setListener(DaysFragmentListener listener) {
        mListener = listener;
    }

    private void colorView() {
        int center = getWidth() / 2;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childCenter = (child.getLeft() + child.getRight()) / 2;
            int distanceFromCenter = Math.abs(center - childCenter);
            float scale = getGaussianScale(distanceFromCenter);

            View contentView = child.findViewById(R.id.ll_content);
            if (contentView != null) {
                contentView.setVisibility(scale >= 0.7 ? View.VISIBLE : View.GONE);
            }

            float minScale = 0.4F;
            float alpha = scale <= minScale ? minScale : scale;
            child.setAlpha(alpha);

            int firstVisibleIndex = layoutManager.findFirstVisibleItemPosition();
            mCurrentIndex = Math.min(mItems.size() - 1, firstVisibleIndex);
            mCurrentIndex = Math.max(0, mCurrentIndex);
            mListener.itemChanged(mItems.get(mCurrentIndex));
        }
    }

    private float getGaussianScale(int distanceFromCenter) {
        int scalingFactor = 250;
        float distance = (float) distanceFromCenter / scalingFactor;
        return (float) Math.pow(Math.E, -Math.pow(distance, 2));
    }

    public void updateData(List<CycleDayItem> items) {
        mItems = items;
        adapter.update(items);
    }

    public Integer getCurrentIndex() {
        return mCurrentIndex;
    }
}
