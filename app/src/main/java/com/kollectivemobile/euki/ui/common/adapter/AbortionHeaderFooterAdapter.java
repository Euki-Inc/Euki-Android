package com.kollectivemobile.euki.ui.common.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.listeners.HeightListener;
import com.kollectivemobile.euki.listeners.LinkListener;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.ui.calendar.reminders.RemindersActivity;
import com.kollectivemobile.euki.utils.TextUtils;
import com.kollectivemobile.euki.utils.Utils;
import com.kollectivemobile.euki.utils.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AbortionHeaderFooterAdapter extends AbstractHeaderFooterWrapperAdapter<AbortionHeaderFooterAdapter.HeaderViewHolder, AbortionHeaderFooterAdapter.FooterViewHolder> {
    private Context mContext;
    private ContentItem mContentItem;
    private HeightListener mHeightListener;
    private LinkListener mLinkListener;

    public AbortionHeaderFooterAdapter(Context context, RecyclerView.Adapter adapter, ContentItem contentItem, HeightListener heightListener, LinkListener linkListener) {
        mContext = context;
        mContentItem = contentItem;
        mHeightListener = heightListener;
        mLinkListener = linkListener;
        setAdapter(adapter);
    }

    @NonNull
    @Override
    public AbortionHeaderFooterAdapter.HeaderViewHolder onCreateHeaderItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_abortion_header, parent, false);
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
        AbortionHeaderFooterAdapter.HeaderViewHolder viewHolder = new AbortionHeaderFooterAdapter.HeaderViewHolder(view);
        return viewHolder;
    }

    @NonNull
    @Override
    public AbortionHeaderFooterAdapter.FooterViewHolder onCreateFooterItemViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_abortion_header, parent, false);
        AbortionHeaderFooterAdapter.FooterViewHolder viewHolder = new AbortionHeaderFooterAdapter.FooterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupFullSpanForGridLayoutManager(recyclerView);
    }

    @Override
    public void onBindHeaderItemViewHolder(@NonNull AbortionHeaderFooterAdapter.HeaderViewHolder holder, int localPosition) {
        holder.tvTitle.setText(mContentItem.getLocalizedTitle());
        holder.llmain.removeAllViews();
        for (ContentItem contentItem : mContentItem.getContentItems()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_abortion_item, holder.llmain, false);
            holder.llmain.addView(view);
            ImageView ivIcon = view.findViewById(R.id.iv_icon);
            TextView tvTitle = view.findViewById(R.id.tv_title);

            boolean hasImage = contentItem.getImageIcon() != null && !contentItem.getImageIcon().isEmpty();
            ivIcon.setVisibility(hasImage ? View.VISIBLE : View.GONE);
            if (hasImage) {
                ivIcon.setImageResource(Utils.getImageId(contentItem.getImageIcon()));
            }

            Spannable spannable = TextUtils.getSpannable(contentItem.getLocalizedTitle(), contentItem.getLinks(), mLinkListener, contentItem.getBoldStrings());
            tvTitle.setText(spannable);
            tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
            tvTitle.setVisibility(spannable.toString().isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBindFooterItemViewHolder(@NonNull AbortionHeaderFooterAdapter.FooterViewHolder holder, int localPosition) {
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

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.ll_main) LinearLayout llmain;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.b_set_reminders)
        void setReminders() {
            mContext.startActivity(RemindersActivity.makeIntent(mContext));
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}

