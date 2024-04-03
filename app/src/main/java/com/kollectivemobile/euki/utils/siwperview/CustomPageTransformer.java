package com.kollectivemobile.euki.utils.siwperview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CustomPageTransformer implements ViewPager2.PageTransformer {

    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);
    }
}
