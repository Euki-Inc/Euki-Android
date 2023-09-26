package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.TileItem;
import com.kollectivemobile.euki.ui.common.holder.TileHolder;
import com.kollectivemobile.euki.utils.Utils;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.DraggableItemAdapter;
import com.kollectivemobile.euki.utils.advrecyclerview.draggable.ItemDraggableRange;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TilesAdapter extends RecyclerView.Adapter<TileHolder> implements DraggableItemAdapter<TileHolder> {
    private WeakReference<Context> mContext;
    private TileHolder.TileListener mTileListener;
    private List<TileItem> mTileItems;
    private Boolean mIsEditing;

    public TilesAdapter(Context context, TileHolder.TileListener tileListener) {
        mIsEditing = false;
        mTileItems = new ArrayList<>();
        mContext = new WeakReference<>(context);
        mTileListener = tileListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public TileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_tile, parent, false);
        return new TileHolder(view, mTileListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TileHolder tileHolder, int position) {
        TileItem item = mTileItems.get(position);

        tileHolder.ibAction.setVisibility(mIsEditing ? View.VISIBLE : View.GONE);
        tileHolder.ibAction.setImageResource(item.getUsed() ? R.drawable.ic_tile_remove : R.drawable.ic_tile_add);
        tileHolder.ivIcon.setImageResource(Utils.getImageId(item.getImageIcon()));

        if (item.getTitle() != null && !item.getTitle().isEmpty()) {
            tileHolder.tvTitle.setText(item.getTitle());
        } else {
            tileHolder.tvTitle.setText(Utils.getLocalized(item.getId()));
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tileHolder.cvMain.getLayoutParams();
        int margin = Utils.dpFromInt(6);
        params.setMargins(margin, margin, margin, margin);
        tileHolder.cvMain.setLayoutParams(params);
    }

    @Override
    public long getItemId(int position) {
        return mTileItems.get(position).getItemId();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mTileItems.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull TileHolder holder, int position, int x, int y) {
        return mTileItems.get(position).getUsed();
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull TileHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final TileItem tileItem = mTileItems.get(fromPosition);
        mTileItems.remove(fromPosition);
        mTileItems.add(toPosition, tileItem);
        if (mTileListener != null) {
            mTileListener.orderChanged(mTileItems);
        }
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return mTileItems.get(dropPosition).getUsed();
    }

    @Override
    public void onItemDragStarted(int position) {
        mTileListener.editStarted();
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    public void update(List<TileItem> tileItems, Boolean isEditing) {
        mIsEditing = isEditing;
        mTileItems.clear();
        mTileItems.addAll(tileItems);
        notifyDataSetChanged();
    }
}
