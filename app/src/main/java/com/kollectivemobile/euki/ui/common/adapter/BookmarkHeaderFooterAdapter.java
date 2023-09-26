package com.kollectivemobile.euki.ui.common.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.utils.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

public class BookmarkHeaderFooterAdapter extends AbstractHeaderFooterWrapperAdapter<BookmarkHeaderFooterAdapter.HeaderViewHolder, BookmarkHeaderFooterAdapter.FooterViewHolder> {

    public BookmarkHeaderFooterAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter);
    }

    @NonNull
    @Override
    public BookmarkHeaderFooterAdapter.HeaderViewHolder onCreateHeaderItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bookmark_header, parent, false);
        BookmarkHeaderFooterAdapter.HeaderViewHolder viewHolder = new BookmarkHeaderFooterAdapter.HeaderViewHolder(view);
        return viewHolder;
    }

    @NonNull
    @Override
    public BookmarkHeaderFooterAdapter.FooterViewHolder onCreateFooterItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bookmark_header, parent, false);
        BookmarkHeaderFooterAdapter.FooterViewHolder viewHolder = new BookmarkHeaderFooterAdapter.FooterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindHeaderItemViewHolder(@NonNull BookmarkHeaderFooterAdapter.HeaderViewHolder holder, int localPosition) {
    }

    @Override
    public void onBindFooterItemViewHolder(@NonNull BookmarkHeaderFooterAdapter.FooterViewHolder holder, int localPosition) {
    }

    @Override
    public int getHeaderItemCount() {
        return 1;
    }

    @Override
    public int getFooterItemCount() {
        return 0;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}

