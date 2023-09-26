package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchAdapter extends RecyclerView.Adapter {
    private List<ContentItem> mItems;
    private SearchListener mListener;
    private WeakReference<Context> mContext;

    public SearchAdapter(Context context, SearchListener listener) {
        mItems = new ArrayList<>();
        mContext = new WeakReference<>(context);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_search, parent, false);
        return new SearchHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHolder rowHolder = (SearchHolder) holder;
        ContentItem contentItem = mItems.get(position);
        String content = contentItem.getLocalizedContent();

        rowHolder.tvTitle.setText(contentItem.getLocalizedTitle());
        rowHolder.tvText.setText(content);
        rowHolder.tvText.setVisibility(content.isEmpty() ? View.GONE : View.VISIBLE);
        rowHolder.vSeparatorTop.setVisibility(View.VISIBLE);
        rowHolder.vSeparatorBottom.setVisibility(position == mItems.size() - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class SearchHolder extends RecyclerView.ViewHolder {
        private SearchListener mListener;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_text) TextView tvText;
        @BindView(R.id.v_separator_top) View vSeparatorTop;
        @BindView(R.id.v_separator_bottom) View vSeparatorBottom;

        public SearchHolder(View itemView, SearchListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.rl_main)
        void onClick() {
            if (mListener != null) {
                mListener.itemSelected(getLayoutPosition());
            }
        }
    }

    public void update(List<ContentItem> contentItems) {
        if (contentItems != null) {
            mItems.clear();
            mItems.addAll(contentItems);
            notifyDataSetChanged();
        }
    }

    public interface SearchListener {
        void itemSelected(int position);
    }
}
