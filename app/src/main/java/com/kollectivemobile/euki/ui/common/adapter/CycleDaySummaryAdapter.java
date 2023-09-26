package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.CycleDayItem;
import com.kollectivemobile.euki.model.SelectableValue;
import com.kollectivemobile.euki.ui.common.views.SelectableButton;
import com.kollectivemobile.euki.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CycleDaySummaryAdapter extends RecyclerView.Adapter {
    private List<SelectableValue> mItems = new ArrayList<>();
    private WeakReference<Context> mContext;

    public CycleDaySummaryAdapter(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cycle_summary_day_selectable_item, parent, false);
        return new CycleSummaryDaysHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CycleSummaryDaysHolder rowHolder = (CycleSummaryDaysHolder) holder;
        SelectableValue item = mItems.get(position);

        rowHolder.sbItem.setTitle(item.getTitle());
        rowHolder.sbItem.setCounter(item.getCounter());
        rowHolder.sbItem.setImageRes(Utils.getImageId(item.getIconName()));
        rowHolder.sbItem.setIsEnabled(false);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class CycleSummaryDaysHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sb_item) SelectableButton sbItem;
        public CycleSummaryDaysHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void update(List<SelectableValue> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }
}
