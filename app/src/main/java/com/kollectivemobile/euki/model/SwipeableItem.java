package com.kollectivemobile.euki.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kollectivemobile.euki.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class SwipeableItem implements Parcelable {
    private String id;
    private String content;
    private String imageResId;

    public SwipeableItem(String id, String text, String imageResId) {
        this.id = id;
        this.content = text;
        this.imageResId = imageResId;
    }

    // Add a constructor that takes a JSONObject as a parameter
    public SwipeableItem(JSONObject json) {
        if (json != null) {
            // Parse the JSON object and initialize the fields
          if (json.has("id")) {
                this.id = json.optString("id");
          }
            if (json.has("content")) {
                this.content = json.optString("content");
            }
            if (json.has("icon")) {
                try {
                    this.imageResId = Utils.decamelcase(json.getString("icon"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    protected SwipeableItem(Parcel in) {
        id = in.readString();
        content = in.readString();
        imageResId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(imageResId);
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

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImageResId() {
        return imageResId;
    }
}
