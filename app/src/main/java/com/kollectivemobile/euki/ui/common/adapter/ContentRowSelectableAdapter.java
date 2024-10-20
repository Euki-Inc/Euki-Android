package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.ContentItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class ContentRowSelectableAdapter extends RecyclerView.Adapter {
    private List<ContentItem> mContentItems;
    private ContentRowSelectableListener mListener;
    private WeakReference<Context> mContext;

    public ContentRowSelectableAdapter(Context context, List<ContentItem> items, ContentRowSelectableListener listener) {
        mContentItems = new ArrayList<>();
        mContentItems.addAll(items);
        mContext = new WeakReference<>(context);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_content_row_selectable, parent, false);
        return new RowHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowHolder rowHolder = (RowHolder) holder;
        ContentItem contentItem = mContentItems.get(position);

        rowHolder.tvTitle.setText(contentItem.getLocalizedTitle());
        rowHolder.vSeparatorTop.setVisibility(View.VISIBLE);
        rowHolder.vSeparatorBottom.setVisibility(position == mContentItems.size() - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mContentItems.size();
    }

    static class RowHolder extends RecyclerView.ViewHolder {
        private ContentRowSelectableListener mListener;
        public TextView tvTitle;
        public View vSeparatorTop;
        public View vSeparatorBottom;

        public RowHolder(View itemView, ContentRowSelectableListener listener) {
            super(itemView);
            mListener = listener;
            tvTitle = itemView.findViewById(R.id.tv_title);
            vSeparatorTop = itemView.findViewById(R.id.v_separator_top);
            vSeparatorBottom = itemView.findViewById(R.id.v_separator_bottom);

            itemView.findViewById(R.id.rl_main).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RowHolder.this.onClick();
                }
            });
        }

        private void onClick() {
            if (mListener != null) {
                mListener.rowSelected(getBindingAdapterPosition() - 1);
            }
        }
    }

    public interface ContentRowSelectableListener {
        void rowSelected(int position);
    }
}
