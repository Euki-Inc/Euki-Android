package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AbortionAdapter extends RecyclerView.Adapter {
    private WeakReference<Context> mContext;
    private AbortionListener mListener;
    private List<ContentItem> mItems;

    public AbortionAdapter(Context context, AbortionListener listener) {
        mItems = new ArrayList<>();
        mContext = new WeakReference<>(context);
        mListener = listener;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_abortion, parent, false);
        return new AbortionHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AbortionHolder abortionHolder = (AbortionHolder) holder;
        ContentItem contentItem = mItems.get(position);

        abortionHolder.ivIcon.setImageResource(Utils.getImageId(contentItem.getImageIcon()));
        abortionHolder.tvTitle.setText(Utils.getLocalized(contentItem.getId()));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)abortionHolder.cvMain.getLayoutParams();
        int margin = Utils.dpFromInt(6);
        params.setMargins(margin, margin, margin, margin);
        abortionHolder.cvMain.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class AbortionHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.cv_main) CardView cvMain;
        public @BindView(R.id.iv_icon) ImageView ivIcon;
        public @BindView(R.id.tv_title) TextView tvTitle;
        private AbortionListener mListener;

        public AbortionHolder(@NonNull View itemView, AbortionListener listener) {
            super(itemView);
            mListener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cv_main)
        void mainClicked() {
            if (mListener != null) {
                mListener.abortionClicked(getLayoutPosition());
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

    public interface AbortionListener {
        void abortionClicked(int position);
    }
}
