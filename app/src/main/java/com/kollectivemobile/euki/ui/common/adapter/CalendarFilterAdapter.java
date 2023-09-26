package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarFilterAdapter extends RecyclerView.Adapter {
    static final Integer VIEW_TYPE_ITEM = 0;
    static final Integer VIEW_TYPE_FOOTER = 1;

    private List<FilterItem> mFilterItems;
    private CalendarFilterListener mListener;
    private Context mContext;

    public CalendarFilterAdapter(Context context, List<FilterItem> filterItems, CalendarFilterListener listener) {
        mFilterItems = filterItems;
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.layout_filter_item, parent, false);
            return new CalendarItemHolder(view, mListener);
        }
        View view = inflater.inflate(R.layout.layout_filter_footer, parent, false);
        return new CalendarFooterHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalendarItemHolder) {
            CalendarItemHolder itemHolder = (CalendarItemHolder)holder;
            FilterItem filterItem = mFilterItems.get(position);
            itemHolder.vCircle.setBackgroundResource(filterItem.getOn() ? R.drawable.ic_calendar_filter_on : R.drawable.ic_calendar_filter_off);
            itemHolder.vCircle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, filterItem.getColor())));
            itemHolder.tvTitle.setText(Utils.getLocalized(filterItem.getTitle()));
        }
    }

    @Override
    public int getItemCount() {
        return mFilterItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mFilterItems.size()) {
            return VIEW_TYPE_ITEM;
        }

        return VIEW_TYPE_FOOTER;
    }

    static class CalendarItemHolder extends RecyclerView.ViewHolder {
        private CalendarFilterListener mListener;
        @BindView(R.id.v_circle) View vCircle;
        @BindView(R.id.tv_title) TextView tvTitle;

        public CalendarItemHolder(View itemView, CalendarFilterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.ll_main)
        void onClick() {
            if (mListener != null) {
                mListener.filterSelected(getLayoutPosition());
            }
        }
    }

    static class CalendarFooterHolder extends RecyclerView.ViewHolder {
        private CalendarFilterListener mListener;

        public CalendarFooterHolder(View itemView, CalendarFilterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.btn_clear)
        void onClickClear() {
            if (mListener != null) {
                mListener.clear();
            }
        }

        @OnClick(R.id.btn_show_results)
        void onClickShowResults() {
            if (mListener != null) {
                mListener.showResults();
            }
        }
    }

    public interface CalendarFilterListener {
        void filterSelected(int position);
        void clear();
        void showResults();
    }
}
