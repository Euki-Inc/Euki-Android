package com.kollectivemobile.euki.model;

public class ExpandableItem {
    private String mId;
    private String mTitle;
    private String mText;
    private Boolean mIsExpanded;
    private ContentItem mContentItem;

    public ExpandableItem(String id, String title, String text, ContentItem contentItem) {
        this.mId = id;
        this.mTitle = title;
        this.mText = text;
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

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Boolean getExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(Boolean expanded) {
        mIsExpanded = expanded;
    }

    public ContentItem getContentItem() {
        return mContentItem;
    }

    public void setContentItem(ContentItem contentItem) {
        mContentItem = contentItem;
    }
}
