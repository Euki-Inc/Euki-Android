package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.FilterItem;
import com.kollectivemobile.euki.ui.common.listeners.FilterItemListener;
import com.kollectivemobile.euki.utils.Utils;
import com.kollectivemobile.euki.utils.ViewUtils;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.DraggableItemAdapter;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.ItemDraggableRange;
import com.kollectivemobile.euki.utils.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyLogFilterAdapter extends RecyclerView.Adapter<DailyLogFilterAdapter.FilterHolder> implements DraggableItemAdapter<DailyLogFilterAdapter.FilterHolder> {
    private List<FilterItem> mFilterItems;
    private FilterItemListener mListener;
    private WeakReference<Context> mContext;

    public DailyLogFilterAdapter(Context context, FilterItemListener listener) {
        mFilterItems = new ArrayList<>();
        mContext = new WeakReference<>(context);
        mListener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_daily_log_item, parent, false);
        return new FilterHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder filterHolder, int position) {
        FilterItem filterItem = mFilterItems.get(position);
        filterHolder.vCircle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext.get(), filterItem.getColor())));
        filterHolder.tvTitle.setText(Utils.getLocalized(filterItem.getTitle()));
        filterHolder.ibChange.setImageResource(filterItem.getOn() ? R.drawable.ic_tile_remove : R.drawable.ic_tile_add);
        filterHolder.ivMove.setVisibility(filterItem.getOn() ? View.VISIBLE : View.GONE);
    }

    @Override
    public long getItemId(int position) {
        return mFilterItems.get(position).getTitle().hashCode();
    }

    @Override
    public int getItemCount() {
        return mFilterItems.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull FilterHolder holder, int position, int x, int y) {
        final View containerView = holder.rlMain;
        final View dragHandleView = holder.ivMove;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull FilterHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (mListener != null) {
            mListener.moveItems(fromPosition, toPosition);
        }
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return mFilterItems.get(dropPosition).getOn();
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    static class FilterHolder extends AbstractDraggableItemViewHolder {
        private FilterItemListener mListener;
        @BindView(R.id.rl_main) View rlMain;
        @BindView(R.id.v_circle) View vCircle;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.ib_change) ImageButton ibChange;
        @BindView(R.id.iv_move) ImageView ivMove;

        public FilterHolder(View itemView, FilterItemListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.ib_change)
        void onClick() {
            if (mListener != null) {
                mListener.filterChanged(getLayoutPosition());
            }
        }
    }

    public void update(List<FilterItem> filterItems) {
        if (filterItems != null) {
            mFilterItems.clear();
            mFilterItems.addAll(filterItems);
            notifyDataSetChanged();
        }
    }
}
