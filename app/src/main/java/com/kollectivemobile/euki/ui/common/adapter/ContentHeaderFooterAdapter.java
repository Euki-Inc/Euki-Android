package com.kollectivemobile.euki.ui.common.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.listeners.HeightListener;
import com.kollectivemobile.euki.listeners.LinkListener;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.utils.TextUtils;
import com.kollectivemobile.euki.utils.Utils;
import com.kollectivemobile.euki.utils.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentHeaderFooterAdapter extends AbstractHeaderFooterWrapperAdapter<ContentHeaderFooterAdapter.HeaderViewHolder, ContentHeaderFooterAdapter.FooterViewHolder> {
    private ContentItem mContentItem;
    private LinkListener mLinkListener;
    private HeightListener mHeightListener;

    public ContentHeaderFooterAdapter(RecyclerView.Adapter adapter, ContentItem contentItem, LinkListener linkListener, HeightListener heightListener) {
        mContentItem = contentItem;
        mLinkListener = linkListener;
        mHeightListener = heightListener;
        setAdapter(adapter);
    }

    @NonNull
    @Override
    public ContentHeaderFooterAdapter.HeaderViewHolder onCreateHeaderItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_top, parent, false);
        ContentHeaderFooterAdapter.HeaderViewHolder viewHolder = new ContentHeaderFooterAdapter.HeaderViewHolder(view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = view.getHeight();
                if (mHeightListener != null) {
                    mHeightListener.heightChanged(height);
                }
            }
        });
        return viewHolder;
    }

    @NonNull
    @Override
    public ContentHeaderFooterAdapter.FooterViewHolder onCreateFooterItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_content_top, parent, false);
        ContentHeaderFooterAdapter.FooterViewHolder viewHolder = new ContentHeaderFooterAdapter.FooterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupFullSpanForGridLayoutManager(recyclerView);
    }

    @Override
    public void onBindHeaderItemViewHolder(@NonNull ContentHeaderFooterAdapter.HeaderViewHolder holder, int localPosition) {
        if (mContentItem == null) {
            return;
        }

        holder.tvTitle.setText(mContentItem.getLocalizedTitle());

        boolean hasImage = mContentItem.getImageIcon() != null && !mContentItem.getImageIcon().isEmpty();
        holder.ivIcon.setVisibility(hasImage ? View.VISIBLE : View.GONE);
        if (hasImage) {
            holder.ivIcon.setImageResource(Utils.getImageId(mContentItem.getImageIcon()));
        }

        String content = mContentItem.getLocalizedContent();
        holder.tvContent.setVisibility(!content.isEmpty() ? View.VISIBLE : View.GONE);
        holder.tvContent.setText(TextUtils.getSpannable(content, mContentItem.getLinks(), mLinkListener, mContentItem.getBoldStrings()));
        holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBindFooterItemViewHolder(@NonNull ContentHeaderFooterAdapter.FooterViewHolder holder, int localPosition) {
    }

    @Override
    public int getHeaderItemCount() {
        return 1;
    }

    @Override
    public int getFooterItemCount() {
        return 0;
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
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.iv_icon) ImageView ivIcon;
        @BindView(R.id.tv_content) TextView tvContent;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}

