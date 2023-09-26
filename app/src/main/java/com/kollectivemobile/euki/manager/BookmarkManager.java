package com.kollectivemobile.euki.manager;

import com.kollectivemobile.euki.model.Bookmark;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.List;

public interface BookmarkManager {
    Boolean isBookmark(String id);
    void getBookmarks(EukiCallback<List<Bookmark>> callback);
    void addBookmark(String id);
    void removeBookmark(String id);
    void removeAllBookmarks();
}
