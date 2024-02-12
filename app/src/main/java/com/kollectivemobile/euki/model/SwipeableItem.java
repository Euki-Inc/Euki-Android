package com.kollectivemobile.euki.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class SwipeableItem implements Parcelable {
    private int id;
    private String content;
    private int imageResId;

    public SwipeableItem(int id, String text, int imageResId) {
        this.id = id;
        this.content = text;
        this.imageResId = imageResId;
    }

    // Add a constructor that takes a JSONObject as a parameter
    public SwipeableItem(JSONObject json) {
        if (json != null) {
            // Parse the JSON object and initialize the fields
            if (json.has("id")) {
                this.id = json.optInt("id");
            }
            if (json.has("content")) {
                this.content = json.optString("content");
            }
            if (json.has("icon")) {
                this.imageResId = json.optInt("icon");
            }
        }
    }

    protected SwipeableItem(Parcel in) {
        id = in.readInt();
        content = in.readString();
        imageResId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeInt(imageResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SwipeableItem> CREATOR = new Creator<SwipeableItem>() {
        @Override
        public SwipeableItem createFromParcel(Parcel in) {
            return new SwipeableItem(in);
        }

        @Override
        public SwipeableItem[] newArray(int size) {
            return new SwipeableItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getImageResId() {
        return imageResId;
    }
}
