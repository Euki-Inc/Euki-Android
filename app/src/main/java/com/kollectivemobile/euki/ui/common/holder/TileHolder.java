package com.kollectivemobile.euki.ui.common.holder;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.TileItem;
import com.kollectivemobile.euki.utils.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TileHolder extends AbstractDraggableItemViewHolder {
    public @BindView(R.id.cv_main) CardView cvMain;
    public @BindView(R.id.ib_action) ImageButton ibAction;
    public @BindView(R.id.iv_icon) ImageView ivIcon;
    public @BindView(R.id.tv_title) TextView tvTitle;

    private TileListener mTileListener;

    public TileHolder(@NonNull View itemView, TileListener tileListener) {
        super(itemView);
        mTileListener = tileListener;
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.cv_main)
    void mainClicked() {
        if (mTileListener != null) {
            mTileListener.tileSelected(getLayoutPosition());
        }
    }

    @OnClick(R.id.ib_action)
    void actionClicked() {
        if (mTileListener != null) {
            mTileListener.actionSelected(getLayoutPosition());
        }
    }

    public interface TileListener {
        void tileSelected(int position);
        void actionSelected(int position);
        void orderChanged(List<TileItem> tileItems);
        void editStarted();
    }
}
