package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.model.ContentItem;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.ArrayList;
import java.util.List;

public class BookmarkManagerImpl implements BookmarkManager {
    private AppSettingsManager mAppSettingsManager;
    private ContentManager mContentManager;

    private List<String> mBookmarkIds;

    public BookmarkManagerImpl(AppSettingsManager appSettingsManager, ContentManager contentManager) {
        this.mAppSettingsManager = appSettingsManager;
        this.mContentManager = contentManager;
        this.mBookmarkIds = new ArrayList<>();
        if (appSettingsManager.getBookmarkIds() != null) {
            this.mBookmarkIds.addAll(appSettingsManager.getBookmarkIds());
        }
    }

    @Override
    public Boolean isBookmark(String id) {
        return mBookmarkIds.contains(id);
    }

    @Override
    public void getBookmarks(EukiCallback<List<Bookmark>> callback) {
        List<Bookmark> bookmarks = new ArrayList<>();

        for (String bookmarkId : mBookmarkIds) {
            ContentItem contentItem = mContentManager.getContentItem(bookmarkId);

            if (contentItem != null) {
                String title = contentItem.getTitle() != null ? contentItem.getTitle() : contentItem.getId();
                Bookmark bookmark = new Bookmark(contentItem.getId(), title, contentItem.getContent(), contentItem);
                bookmarks.add(bookmark);
            }
        }

        callback.onSuccess(bookmarks);
    }

    @Override
    public void addBookmark(String id) {
        mBookmarkIds.add(id);
        mAppSettingsManager.saveBookmarks(mBookmarkIds);
    }

    @Override
    public void removeBookmark(String id) {
        mBookmarkIds.remove(id);
        mAppSettingsManager.saveBookmarks(mBookmarkIds);
    }

    @Override
    public void removeAllBookmarks() {
        mBookmarkIds.clear();
        mAppSettingsManager.saveBookmarks(mBookmarkIds);
    }
}
