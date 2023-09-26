package com.kollectivemobile.euki.model.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "ReminderItem")
public class ReminderItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "alert_id")
    private String alertId;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "text")
    private String mText;
    @ColumnInfo(name = "date")
    private Date mDate;
    @ColumnInfo(name = "repeat_days")
    private Integer mRepeatDays;
    @ColumnInfo(name = "last_alert")
    private Date mLastAlert;

    public ReminderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Integer getRepeatDays() {
        return mRepeatDays;
    }

    public void setRepeatDays(Integer repeatDays) {
        mRepeatDays = repeatDays;
    }

    public Date getLastAlert() {
        return mLastAlert;
    }

    public void setLastAlert(Date lastAlert) {
        mLastAlert = lastAlert;
    }

    public ReminderItem copy() {
        ReminderItem reminderItem = new ReminderItem();
        reminderItem.mTitle = mTitle;
        reminderItem.mText = mText;
        reminderItem.mDate = mDate;
        reminderItem.mLastAlert = mLastAlert;
        reminderItem.mRepeatDays = mRepeatDays;
        reminderItem.id = id;
        return reminderItem;
    }
}
