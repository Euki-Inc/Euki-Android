package com.kollectivemobile.euki.ui.common.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.utils.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyLogFilterFooterAdapter extends AbstractHeaderFooterWrapperAdapter<DailyLogFilterFooterAdapter.HeaderViewHolder, DailyLogFilterFooterAdapter.FooterViewHolder> {
    private View.OnClickListener mOnClickListener;

    public DailyLogFilterFooterAdapter(RecyclerView.Adapter adapter, View.OnClickListener onClickListener) {
        setAdapter(adapter);
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateHeaderItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_item_footer, parent, false);
        HeaderViewHolder viewHolder = new HeaderViewHolder(view);
        return viewHolder;
    }

    @NonNull
    @Override
    public FooterViewHolder onCreateFooterItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_item_footer, parent, false);
        FooterViewHolder viewHolder = new FooterViewHolder(view, mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupFullSpanForGridLayoutManager(recyclerView);
    }

    @Override
    public void onBindHeaderItemViewHolder(@NonNull HeaderViewHolder holder, int localPosition) {
    }

    @Override
    public void onBindFooterItemViewHolder(@NonNull FooterViewHolder holder, int localPosition) {
        holder.btnAction.setText(App.getContext().getString(R.string.done));
    }

    @Override
    public int getHeaderItemCount() {
        return 0;
    }

    @Override
    public int getFooterItemCount() {
        return 1;
    }

    public void setupFullSpanForGridLayoutManager(RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (!(lm instanceof GridLayoutManager)) {
            return;
        }

        final GridLayoutManager glm = (GridLayoutManager) lm;
        final GridLayoutManager.SpanSizeLookup origSizeLookup = glm.getSpanSizeLookup();
        final int spanCount = glm.getSpanCount();

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final long segmentedPosition = getSegmentedPosition(position);
                final int segment = extractSegmentPart(segmentedPosition);
                final int offset = extractSegmentOffsetPart(segmentedPosition);

                if (segment == SEGMENT_TYPE_NORMAL) {
                    return origSizeLookup.getSpanSize(offset);
                } else {
                    return spanCount; // header or footer
                }
            }
        });
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView){
            super(itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_action) Button btnAction;

        private View.OnClickListener mOnClickListener;

        public FooterViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnClickListener = onClickListener;
        }

        @OnClick(R.id.btn_action)
        void actionPressed(View view) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(view);
            }
        }
    }
}
