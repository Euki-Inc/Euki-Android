package com.kollectivemobile.euki.ui.bookmarks;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.BookmarkManager;
import com.kollectivemobile.euki.manager.ContentManager;
import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.networking.EukiCallback;
import com.kollectivemobile.euki.networking.ServerError;
import com.kollectivemobile.euki.ui.common.BaseFragment;
import com.kollectivemobile.euki.ui.common.SwipeController;
import com.kollectivemobile.euki.ui.common.SwipeControllerActions;
import com.kollectivemobile.euki.ui.common.adapter.BookmarkAdapter;
import com.kollectivemobile.euki.ui.common.adapter.BookmarkHeaderFooterAdapter;
import com.kollectivemobile.euki.ui.home.content.ContentItemActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class BookmarksFragment extends BaseFragment implements BookmarkAdapter.BookMarksListener {
    @Inject BookmarkManager mBookmarkManager;
    @Inject ContentManager mContentManager;
    @BindView(R.id.rv_main) RecyclerView rvMain;
    @BindView(R.id.ll_empty) View vEmpty;

    private BookmarkAdapter mBookmarkAdapter;
    private List<Bookmark> mBookmarks;
    private SwipeController swipeController = null;

    public static BookmarksFragment newInstance() {
        Bundle args = new Bundle();
        BookmarksFragment fragment = new BookmarksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((App)getActivity().getApplication()).getAppComponent().inject(this);
        setUIElements();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestBookmarks();
    }

    @Override
    protected View onCreateViewCalled(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public boolean showBack() {
        return true;
    }

    private void setUIElements() {
        mBookmarks = new ArrayList<>();
        mBookmarkAdapter = new BookmarkAdapter(getActivity(), this);
        BookmarkHeaderFooterAdapter headerFooterAdapter = new BookmarkHeaderFooterAdapter(mBookmarkAdapter);
        rvMain.setAdapter(headerFooterAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                Bookmark bookmark = mBookmarks.get(position - 1);
                mBookmarkManager.removeBookmark(bookmark.getId());
                requestBookmarks();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvMain);

        rvMain.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void requestBookmarks() {
        mBookmarkManager.getBookmarks(new EukiCallback<List<Bookmark>>() {
            @Override
            public void onSuccess(List<Bookmark> bookmarks) {
                mBookmarks.clear();
                mBookmarks.addAll(bookmarks);
                mBookmarkAdapter.update(mBookmarks);
                vEmpty.setVisibility(bookmarks.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(ServerError serverError) {
                showError(serverError.getMessage());
            }
        });
    }

    @Override
    public void bookmarkSelected(int position) {
        Bookmark bookmark = mBookmarks.get(position);

        Intent intent;
        if (bookmark.getContentItem().getParent() != null) {
            ContentItem parentItem = mContentManager.getContentItem(bookmark.getContentItem().getParent().getId());
            intent = ContentItemActivity.makeIntent(getActivity(), parentItem, bookmark.getContentItem());
        } else {
            intent = ContentItemActivity.makeIntent(getActivity(), bookmark.getContentItem());
        }
        startActivity(intent);
    }
}
