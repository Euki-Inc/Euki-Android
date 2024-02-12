package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.listeners.LinkListener;
import com.kollectivemobile.euki.manager.BookmarkManager;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.model.SwipeableItem;
import com.kollectivemobile.euki.utils.TextUtils;
import com.kollectivemobile.euki.utils.advrecyclerview.expandable.ExpandableItemState;
import com.kollectivemobile.euki.utils.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.kollectivemobile.euki.utils.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.kollectivemobile.euki.utils.siwperview.CustomContainerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentRowExpandableAdapter extends AbstractExpandableItemAdapter<ContentRowExpandableAdapter.TitleHolder, ContentRowExpandableAdapter.TextHolder> {
    private List<ContentItem> mContentItems;

    private ContentRowExpandableListener mListener;
    private LinkListener mLinkListener;
    private WeakReference<Context> mContext;
    private BookmarkManager mBookmarkManager;

    // Inside ContentItem class
    private List<SwipeableItem> mSwiperItems;


    public ContentRowExpandableAdapter(Context context, List<ContentItem> items, ContentRowExpandableListener listener, LinkListener linkListener, BookmarkManager bookmarkManager) {
        mContentItems = new ArrayList<>();
        mContentItems.addAll(items);
        mContext = new WeakReference<>(context);
        mListener = listener;
        mLinkListener = linkListener;
        mBookmarkManager = bookmarkManager;
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return mContentItems.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mContentItems.get(groupPosition).getItemId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mContentItems.get(groupPosition).getItemId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @NonNull
    @Override
    public TitleHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_content_row_expandable_title, parent, false);
        return new TitleHolder(view, mListener);
    }

    @NonNull
    @Override
    public TextHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_content_row_expandable_text, parent, false);
        return new TextHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(@NonNull final TitleHolder holder, int groupPosition, int viewType) {
        final ContentItem contentItem = mContentItems.get(groupPosition);

        holder.tvTitle.setText(contentItem.getLocalizedTitle());
        holder.vSeparatorTop.setVisibility(View.VISIBLE);
        holder.vSeparatorBottom.setVisibility(groupPosition == mContentItems.size() - 1 ? View.VISIBLE : View.GONE);

        final ExpandableItemState expandState = holder.getExpandState();

        if (expandState.isUpdated()) {
            holder.ivArrow.setImageResource(expandState.isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
            holder.vSeparatorBottom.setVisibility(expandState.isExpanded() ? View.GONE : groupPosition == mContentItems.size() - 1 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBindChildViewHolder(@NonNull final TextHolder holder, int groupPosition, int childPosition, int viewType) {
        final ContentItem contentItem = mContentItems.get(groupPosition);

        Spannable spannable = TextUtils.getSpannable(contentItem.getLocalizedContent(), contentItem.getLinks(), mLinkListener, contentItem.getBoldStrings());
        holder.tvTitle.setText(spannable);
        holder.tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
        holder.vSeparatorBottom.setVisibility(groupPosition == mContentItems.size() - 1 ? View.VISIBLE : View.GONE);

        int resId = mBookmarkManager.isBookmark(contentItem.getId()) ? R.drawable.ic_bookmark_on : R.drawable.ic_bookmark_off;
        holder.ivBookmark.setImageResource(resId);
        holder.ivBookmark.setOnClickListener(view -> {
            if (mBookmarkManager.isBookmark(contentItem.getId())) {
                mBookmarkManager.removeBookmark(contentItem.getId());
                holder.ivBookmark.setImageResource(R.drawable.ic_bookmark_off);
            } else {
                mBookmarkManager.addBookmark(contentItem.getId());
                holder.ivBookmark.setImageResource(R.drawable.ic_bookmark_on);
            }
        });

        // Check if the contentItem has swipe_pager items
        if (contentItem.getSwiperItems() != null && !contentItem.getSwiperItems().isEmpty()) {
            holder.swiperView.setItems(contentItem.getSwiperItems());
            holder.swiperView.setVisibility(View.VISIBLE);
        } else {
            holder.swiperView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(@NonNull TitleHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    static class TitleHolder extends AbstractExpandableItemViewHolder {
        private ContentRowExpandableListener mListener;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.iv_arrow) ImageView ivArrow;
        @BindView(R.id.v_separator_top) View vSeparatorTop;
        @BindView(R.id.v_separator_bottom) View vSeparatorBottom;

        public TitleHolder(View itemView, ContentRowExpandableListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.rl_main)
        void onClick() {
            if (mListener != null) {
                mListener.rowExpandableSelected(getLayoutPosition() - 1);
            }
        }
    }

    static class TextHolder extends AbstractExpandableItemViewHolder {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.v_separator_bottom) View vSeparatorBottom;
        @BindView(R.id.iv_bookmark) ImageView ivBookmark;

        @BindView(R.id.swiper_view)
        CustomContainerView swiperView;

        public TextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ContentRowExpandableListener {
        void rowExpandableSelected(int position);
    }
}
