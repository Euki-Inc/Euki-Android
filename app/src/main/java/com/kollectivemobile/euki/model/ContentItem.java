package com.kollectivemobile.euki.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContentItem implements Parcelable {
    protected long mItemId;
    private String mId;
    private String mTitle;
    private String mImageIcon;
    private String mContent;
    private Boolean mIsExpandable;
    private ContentItem mParent;
    private List<ContentItem> mContentItems;
    private List<ContentItem> mExpandableItems;
    private List<ContentItem> mSelectableItems;
    private List<ContentItem> mSelectableRowItems;

   private List<SwipeableItem> mSwiperItems;
    private String mSource;
    private Map<String, String> mLinks;
    private List<String> mBoldStrings;
    private Boolean mIsAbottionItem;

    public ContentItem() {
        this.mContentItems = new ArrayList<>();
        this.mExpandableItems = new ArrayList<>();
        this.mSelectableItems = new ArrayList<>();
        this.mSelectableRowItems = new ArrayList<>();
        this.mBoldStrings = new ArrayList<>();
        this.mSwiperItems = new ArrayList<>();
        this.mLinks = new HashMap<>();
    }

    public ContentItem(String id) {
        this();
        mId = id;
    }

    public ContentItem(String id, String imageIcon) {
        this(id);
        this.mImageIcon = imageIcon;
    }

    public ContentItem(JSONObject json) {
        this(json, 0);
    }

    public ContentItem(JSONObject json, long id) {
        this();
        mItemId = id;

        if (json == null) {
            return;
        }
        try {
            if (json.has("id")){
                this.mId = json.getString("id");
            }
            if (json.has("title")) {
                this.mTitle = json.getString("title");
            }
            if (json.has("icon")) {
                this.mImageIcon = Utils.decamelcase(json.getString("icon"));
                Log.d("IMAGES!!", mImageIcon);
            }
            if (json.has("content")) {
                this.mContent = json.getString("content");
            }
            if (json.has("source")) {
                this.mSource = json.getString("source");
            }

            if (json.has("content_items")) {
                JSONArray contentItemsArray = json.getJSONArray("content_items");
                for (int i = 0; i < contentItemsArray.length(); i++) {
                    JSONObject itemJson = contentItemsArray.getJSONObject(i);
                    this.mContentItems.add(new ContentItem(itemJson, i));
                }
            }

            if (json.has("expandable_items")) {
                JSONArray expandableItemsArray = json.getJSONArray("expandable_items");
                for (int i = 0; i < expandableItemsArray.length(); i++) {
                    JSONObject itemJson = expandableItemsArray.getJSONObject(i);
                    this.mExpandableItems.add(new ContentItem(itemJson, i));
                }
            }

            if (json.has("selectable_items")) {
                JSONArray selectableItemsArray = json.getJSONArray("selectable_items");
                for (int i = 0; i < selectableItemsArray.length(); i++) {
                    JSONObject itemJson = selectableItemsArray.getJSONObject(i);
                    this.mSelectableItems.add(new ContentItem(itemJson, i));
                }
            }

            if (json.has("selectable_row_items")) {
                JSONArray selectableRowItemsArray = json.getJSONArray("selectable_row_items");
                for (int i = 0; i < selectableRowItemsArray.length(); i++) {
                    JSONObject itemJson = selectableRowItemsArray.getJSONObject(i);
                    this.mSelectableRowItems.add(new ContentItem(itemJson, i));
                }
            }

            if (json.has("links")) {
                JSONObject linksJson = json.getJSONObject("links");
                Iterator<String> iterator = linksJson.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = linksJson.getString(key);
                    mLinks.put(key, value);
                }
            }

            if (json.has("bold_strings")) {
                JSONArray jsonArray = json.getJSONArray("bold_strings");
                for (int i=0; i<jsonArray.length(); i++) {
                    String string = jsonArray.getString(i);
                    mBoldStrings.add(string);
                }
            }

         /*   if (json.has("swipe_pager")) {
                JSONArray swiperItems = json.getJSONArray("swipe_pager");
                for (int i = 0; i < swiperItems.length(); i++) {
                    JSONObject itemJson = swiperItems.getJSONObject(i);
                    this.mSwiperItems.add(new SwipeableItem(itemJson));
                }
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected ContentItem(Parcel in) {
        mItemId = in.readLong();
        mId = in.readString();
        mTitle = in.readString();
        mImageIcon = in.readString();
        mContent = in.readString();
        byte tmpMIsExpandable = in.readByte();
        mIsExpandable = tmpMIsExpandable == 0 ? null : tmpMIsExpandable == 1;
        mParent = in.readParcelable(ContentItem.class.getClassLoader());
        mContentItems = in.createTypedArrayList(ContentItem.CREATOR);
        mExpandableItems = in.createTypedArrayList(ContentItem.CREATOR);
        mSelectableItems = in.createTypedArrayList(ContentItem.CREATOR);
        mSelectableRowItems = in.createTypedArrayList(ContentItem.CREATOR);
        mSource = in.readString();
        mBoldStrings = in.createStringArrayList();
        mSwiperItems = in.createTypedArrayList(SwipeableItem.CREATOR);

        byte tmpMIsAbottionItem = in.readByte();
        mIsAbottionItem = tmpMIsAbottionItem == 0 ? null : tmpMIsAbottionItem == 1;
        mLinks = new Gson().fromJson(in.readString(), new TypeToken<Map<String, String>>(){}.getType());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mItemId);
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mImageIcon);
        dest.writeString(mContent);
        dest.writeByte((byte) (mIsExpandable == null ? 0 : mIsExpandable ? 1 : 2));
        dest.writeParcelable(mParent, flags);
        dest.writeTypedList(mContentItems);
        dest.writeTypedList(mExpandableItems);
        dest.writeTypedList(mSwiperItems);
        dest.writeTypedList(mSelectableItems);
        dest.writeTypedList(mSelectableRowItems);
        dest.writeString(mSource);
        dest.writeStringList(mBoldStrings);
        dest.writeByte((byte) (mIsAbottionItem == null ? 0 : mIsAbottionItem ? 1 : 2));
        dest.writeString(mLinks == null ? "" : new Gson().toJson(mLinks));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContentItem> CREATOR = new Creator<ContentItem>() {
        @Override
        public ContentItem createFromParcel(Parcel in) {
            return new ContentItem(in);
        }

        @Override
        public ContentItem[] newArray(int size) {
            return new ContentItem[size];
        }
    };

    public Boolean isDeeperLevel() {
        return this.mSelectableItems.size() == 0;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ContentItem) {
            ContentItem compareObject = (ContentItem) object;
            return this.getId().equals(compareObject.getId());
        }
        return super.equals(object);
    }

    public long getItemId() {
        return mItemId;
    }

    public void setItemId(long itemId) {
        mItemId = itemId;
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

    public String getImageIcon() {
        return mImageIcon;
    }

    public void setImageIcon(String imageIcon) {
        mImageIcon = imageIcon;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Boolean getExpandable() {
        return mIsExpandable;
    }

    public void setExpandable(Boolean expandable) {
        mIsExpandable = expandable;
    }

    public ContentItem getParent() {
        return mParent;
    }

    public void setParent(ContentItem parent) {
        mParent = parent;
    }

    public List<ContentItem> getContentItems() {
        return mContentItems;
    }

    public void setContentItems(List<ContentItem> contentItems) {
        mContentItems = contentItems;
    }

    public List<ContentItem> getExpandableItems() {
        return mExpandableItems;
    }

    public void setExpandableItems(List<ContentItem> expandableItems) {
        mExpandableItems = expandableItems;
    }

    public List<SwipeableItem> getSwiperItems() {
        return mSwiperItems;
    }

    public void setSwiperItems(List<SwipeableItem> expandableWithSwiperItems) {
        mSwiperItems = expandableWithSwiperItems;
    }

    public List<ContentItem> getSelectableItems() {
        return mSelectableItems;
    }

    public void setSelectableItems(List<ContentItem> selectableItems) {
        mSelectableItems = selectableItems;
    }

    public List<ContentItem> getSelectableRowItems() {
        return mSelectableRowItems;
    }

    public void setSelectableRowItems(List<ContentItem> selectableRowItems) {
        mSelectableRowItems = selectableRowItems;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public Map<String, String> getLinks() {
        return mLinks;
    }

    public void setLinks(Map<String, String> links) {
        mLinks = links;
    }

    public List<String> getBoldStrings() {
        return mBoldStrings;
    }

    public void setBoldStrings(List<String> boldStrings) {
        mBoldStrings = boldStrings;
    }

    public Boolean getAbottionItem() {
        return mIsAbottionItem;
    }

    public void setAbottionItem(Boolean abottionItem) {
        mIsAbottionItem = abottionItem;
    }

    public String getLocalizedTitle() {
        if (mTitle != null && !mTitle.isEmpty()) {
            return Utils.getLocalized(mTitle);
        }
        if (mId != null && !mId.isEmpty()) {
            return Utils.getLocalized(mId);
        }
        return "";
    }

    public String getLocalizedContent() {
        StringBuffer stringBuffer = new StringBuffer();

        if (mContent != null && !mContent.isEmpty()) {
            stringBuffer.append(Utils.getLocalized(mContent));
        }
        if (mSource != null && !mSource.isEmpty()) {
            if (!stringBuffer.toString().isEmpty()) {
                stringBuffer.append("\n\n");
            }
            String source = String.format(App.getContext().getString(R.string.source_format), Utils.getLocalized(mSource));
            stringBuffer.append(Utils.getLocalized(source));
        }

        return stringBuffer.toString();
    }
}
