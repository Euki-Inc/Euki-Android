package com.kollectivemobile.euki.model;

public class Bookmark {
    private String mId;
    private String mTitle;
    private String mContent;
    private ContentItem mContentItem;

    public Bookmark() {
        this.mId = "";
        this.mTitle = "";
        this.mContent = "";
        this.mContentItem = new ContentItem();
    }

    public Bookmark(String id, String title, String content, ContentItem contentItem) {
        this.mId = id;
        this.mTitle = title;
        this.mContent = content;
        this.mContentItem = contentItem;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public ContentItem getContentItem() {
        return mContentItem;
    }

    public void setContentItem(ContentItem contentItem) {
        mContentItem = contentItem;
    }
}
