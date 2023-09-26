package com.kollectivemobile.euki.ui.common.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookmarkAdapter extends RecyclerView.Adapter {
    private List<Bookmark> mBookmarks;
    private BookMarksListener mListener;
    private WeakReference<Context> mContext;

    public BookmarkAdapter(Context context, BookMarksListener listener) {
        mBookmarks = new ArrayList<>();
        mContext = new WeakReference<>(context);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_bookmark, parent, false);
        return new BookmarkHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookmarkHolder rowHolder = (BookmarkHolder) holder;
        Bookmark bookmark = mBookmarks.get(position);

        rowHolder.tvTitle.setText(Utils.getLocalized(bookmark.getTitle()));
        rowHolder.tvText.setText(Utils.getLocalized(bookmark.getContent()));
        rowHolder.tvText.setVisibility(bookmark.getContent() == null || bookmark.getContent().isEmpty() ? View.GONE : View.VISIBLE);
        rowHolder.vSeparatorTop.setVisibility(View.VISIBLE);
        rowHolder.vSeparatorBottom.setVisibility(position == mBookmarks.size() - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mBookmarks.size();
    }

    public class BookmarkHolder extends RecyclerView.ViewHolder {
        private BookMarksListener mListener;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_text) TextView tvText;
        @BindView(R.id.v_separator_top) View vSeparatorTop;
        @BindView(R.id.v_separator_bottom) View vSeparatorBottom;

        public BookmarkHolder(View itemView, BookMarksListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
        }

        @OnClick(R.id.rl_main)
        void onClick() {
            if (mListener != null) {
                mListener.bookmarkSelected(getLayoutPosition() - 1);
            }
        }
    }

    public void update(List<Bookmark> bookmarks) {
        if (bookmarks != null) {
            mBookmarks.clear();
            mBookmarks.addAll(bookmarks);
            notifyDataSetChanged();
        }
    }

    public interface BookMarksListener {
        void bookmarkSelected(int position);
    }
}
