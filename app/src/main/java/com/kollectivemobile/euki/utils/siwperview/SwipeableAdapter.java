package com.kollectivemobile.euki.utils.siwperview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.SwipeableItem;
import com.kollectivemobile.euki.utils.Utils;

import java.util.List;

public class SwipeableAdapter extends RecyclerView.Adapter<SwipeableAdapter.ViewHolder> {
    private final List<SwipeableItem> items;

    public SwipeableAdapter(List<SwipeableItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // Adjust margins
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0); // Adjust as needed

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SwipeableItem currentItem = items.get(position);
        holder.textView.setText(Utils.getLocalized(currentItem.getContent()));
        holder.imageView.setImageResource(Utils.getImageId(currentItem.getImageResId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.contentText);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
