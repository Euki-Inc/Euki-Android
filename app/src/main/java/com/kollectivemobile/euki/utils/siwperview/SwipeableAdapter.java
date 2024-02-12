package com.kollectivemobile.euki.utils.siwperview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.SwipeableItem;

import java.util.List;

public class SwipeableAdapter extends PagerAdapter {
    private final List<SwipeableItem> items;

    public SwipeableAdapter(List<SwipeableItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, container, false);

       TextView textView = itemView.findViewById(R.id.textView);
        ImageView imageView = itemView.findViewById(R.id.imageView);

        //Set data to the views based on the SwipeableItem at the current position
        SwipeableItem currentItem = items.get(position);
        textView.setText(currentItem.getContent());
        imageView.setImageResource(currentItem.getImageResId());

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
