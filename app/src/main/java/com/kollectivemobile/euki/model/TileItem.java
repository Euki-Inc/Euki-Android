package com.kollectivemobile.euki.model;

public class TileItem extends ContentItem {
    private Boolean mIsUsed;

    public TileItem(long tileId, String id, String imageIcon) {
        super(id, imageIcon);
        mItemId = tileId;
    }

    public Boolean getUsed() {
        return mIsUsed;
    }

    public void setUsed(Boolean used) {
        mIsUsed = used;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TileItem)) {
            return false;
        }

        TileItem that = (TileItem) other;

        return this.getItemId() == that.getItemId();
    }

    @Override
    public int hashCode() {
        return (int)mItemId;
    }
}
