package com.kollectivemobile.euki.utils.siwperview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.SwipeableItem;

import java.util.List;

public class CustomContainerView extends RelativeLayout {
    private ViewPager2 viewPager;
    private LinearLayout dotsLayout;

    public CustomContainerView(Context context) {
        super(context);
        init();
    }

    public CustomContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_container_view, this);
        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);

        // Set up ViewPager2 listener for page changes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
            }
        });

    }

    public void setItems(List<SwipeableItem> items) {
        // Clear the existing items in the adapter
        viewPager.setAdapter(null);

        // Create a new adapter with the updated items
        SwipeableAdapter adapter = new SwipeableAdapter(items);
        viewPager.setAdapter(adapter);

        // Add dots based on the number of pages
        addDots(items.size());

        // Update dots when the first page is selected
        updateDots(0);
    }

    private void addDots(int count) {
        // Clear existing dots
        dotsLayout.removeAllViews();

        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(getContext());
            dot.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_selector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dot, params);
        }
    }

    private void updateDots(int position) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsLayout.getChildAt(i);
            dot.setSelected(i == position);
        }
    }
}
